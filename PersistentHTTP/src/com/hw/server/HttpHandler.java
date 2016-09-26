package com.hw.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;
import com.hw.filecache.AccessCounter;
import com.hw.filecache.FileCacheLFU;

public class HttpHandler {

    private boolean isValidRequest = false;
    String httpVersion = null;
    boolean connectionKeepAlive;
    private ServerSocket serverSocket;
    private Date startDate;
    Socket client;
    AccessCounter accessCounter = new AccessCounter();
    FileCacheLFU fileCacheLFU = new FileCacheLFU();
    Path path;
    ReentrantLock lock = new ReentrantLock();

    HttpHandler(Socket client, ServerSocket serverSocket, Date startDate) {
        this.serverSocket = serverSocket;
        this.startDate = startDate;
        this.client = client;
    }

    public void executeCommand() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream out = new PrintStream(client.getOutputStream());
            String fileName = "";
            String token = null;
            String classPath = TinyHttpd.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            File file = null;
            try {
                System.out.println("I/O setup done");

                String line = in.readLine();
                while (line != null) {
                    System.out.println(line);
                    if (line.equals("")) {
                        break;
                    }

                    StringTokenizer tokenizer = new StringTokenizer(line, " ");

                    while (tokenizer.hasMoreTokens()) {
                        token = tokenizer.nextToken();
                        if (token.startsWith("HTTP")) //checking version of the HTTP connection.
                        {
                            if (token.contains("/1.0")) {
                                httpVersion = "HTTP/1.0";
                            } else if (token.contains("/1.1")) {
                                httpVersion = "HTTP/1.1";
                            }
                        }
                        if (token.equals("Connection:")) //parsing request to get connection type
                        {
                            if (tokenizer.nextToken().equals("keep-alive")) //checking for connection type
                            {
                                connectionKeepAlive = true;
                            }
                        }

                        if (token.equals("GET") || token.equals("POST") || token.equals("HEAD")) {
                            isValidRequest = true;
                            fileName = tokenizer.nextToken();
                            if (fileName.equals("/")) {
                                fileName = "index.html";
                            } else {
                                fileName = fileName.substring(1);
                            }
                        }
                    }
                    line = in.readLine();
                }
                System.out.println(line);

                file = new File(classPath + fileName);
                System.out.println(file.getName() + " requested.");
                if (file.exists()) {
                    lock.lock();
                    path = Paths.get("./src/" + fileName);
                    try {

                        accessCounter.increment(path);
                        System.out.println("Access Counter");
                        System.out.println(path + " " + accessCounter.getCount(path));
                        fileCacheLFU.replace(path);
                        String content = fileCacheLFU.fetch(path);
                        System.out.println("Access Counter content");
                        System.out.println(content);

                    } finally {
                        lock.unlock();
                    }
                }
                sendFile(out, file);

                out.flush();
                if (client.isClosed()) {
                    System.out.println("client is closed before finally");
                }

            } finally {
                if (httpVersion.equals("HTTP/1.0") && connectionKeepAlive == false) {
                    client.close();
                    System.out.println("HTTP/1.0 and Keep-alive = false: Connection is closed.");
                } else if (httpVersion.equals("HTTP/1.0") && connectionKeepAlive == true) {
                    client.setKeepAlive(true);
                    System.out.println("client keep alive: " + client.getKeepAlive());
                    client.setSoTimeout(3000);
                    System.out.println("So_timeout set: " + client.getSoTimeout());
                } else if (httpVersion.equals("HTTP/1.1")) {
                    client.setKeepAlive(true);
                    System.out.println("client keep alive: " + client.getKeepAlive());
                    client.setSoTimeout(3000);
                    System.out.println("So_timeout set: " + client.getSoTimeout());
                }
                if (file == null) {
                    client.close();
                }
                if (!file.exists()) {
                    client.close();
                }
            }
        } catch (SocketTimeoutException e) {
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("Socket timeout, Connection closed.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void sendFile(PrintStream out, File file) {
        String statusCode = "";
        boolean isFound = true;
        DataInputStream fin = null;
        try {
            if (file.exists()) {
                statusCode = "200 OK";
            } else {
                statusCode = "404 Not Found";
                isFound = false;
                System.out.println("File not found");
            }
            if (!isValidRequest) {
                statusCode = "501 Not Implemented";
                isFound = false;
                System.out.println("Command Not Implemented");
            }
            if (isFound) {
                fin = new DataInputStream(new FileInputStream(file));
            }
            try {

                out.println("HTTP/1.0 " + statusCode);
                String fileName = file.getName();
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                if (isFound) {
                    if (fileExt.equals("htm") || fileExt.equals("html")) {
                        out.println("Content-Type: text/html");
                    } else if (fileExt.equals("png")) {
                        out.println("Content-Type: image/png");
                    } else if (fileExt.equals("jpeg") || fileExt.equals("jpg")) {
                        out.println("Content-Type: image/jpeg");
                    }

                    int len = (int) file.length();
                    out.println("Content-Length: " + len);
                    out.println("Server: " + serverSocket.getInetAddress());
                    out.println("Last-Modified: " + new Date());
                    out.println("Date: " + startDate);
                    out.println("");

                    byte buf[] = new byte[len];
                    fin.readFully(buf);
                    out.write(buf, 0, len);
                }
                out.flush();
            } finally {
                if (fin != null) {
                    fin.close();
                }
                isValidRequest = false;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
