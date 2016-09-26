package com.hw;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.File;
import java.util.Date;
import java.util.StringTokenizer;

public class TinyHttpd3 implements Runnable {

    private static final int PORT = 8888;
    private ServerSocket serverSocket;
    private Socket client;
    private Date startDate;
    private boolean isValidRequest = false;

    public void init() {
        try {
            try {
                serverSocket = new ServerSocket(PORT);
                startDate = new Date();
                System.out.println("Socket created.");

                while (true) {
                    System.out.println("Listening to a connection on the local port "
                            + serverSocket.getLocalPort() + "...");
                    client = serverSocket.accept();
                    System.out.println("\nA connection established with the remote port "
                            + client.getPort() + " at "
                            + client.getInetAddress().toString());
                    new Thread(new ThreadPool(this)).start();
                }
            } finally {
                serverSocket.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void executeCommand(Socket client) {
        try {
            client.setSoTimeout(30000);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream out = new PrintStream(client.getOutputStream());
            String fileName = "";
            String classPath = TinyHttpd2.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            try {
                System.out.println("I/O setup done");

                String line = in.readLine();
                while (line != null) {
                    System.out.println(line);
                    if (line.equals("")) {
                        break;
                    }
                    
                    StringTokenizer tokenizer = new StringTokenizer(line, " ");
                    if (!tokenizer.hasMoreTokens()) {
                        break;
                    }

                    String methodTocken = tokenizer.nextToken();
                    if (methodTocken.equals("GET") || methodTocken.equals("POST") || methodTocken.equals("HEAD")) {
                        isValidRequest = true;
                        fileName = tokenizer.nextToken();
                        if (fileName.equals("/")) {
                            fileName = "index.html";
                        } else {
                            fileName = fileName.substring(1);
                        }
                    }

                    line = in.readLine();
                }
                System.out.println(line);

                File file = new File(classPath + fileName);
                System.out.println(file.getName() + " requested.");
                sendFile(out, file);

                out.flush();

            } finally {
                in.close();
                out.close();
                client.close();
                System.out.println("A connection is closed.");
            }
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
            if(!isValidRequest){
                statusCode = "501 Not Implemented";
                isFound = false;
                System.out.println("Command Not Implemented");
            }
            System.out.println(statusCode + " " + isFound);
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

    public void run() {
        try {
            executeCommand(client);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void execute() {
        try {
            executeCommand(client);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        TinyHttpd3 server = new TinyHttpd3();
        server.init();
    }

}
