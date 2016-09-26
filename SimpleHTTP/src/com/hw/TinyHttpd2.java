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
import java.util.StringTokenizer;

public class TinyHttpd2 {

    private static final int PORT = 8888;
    private ServerSocket serverSocket;

    public void init() {
        try {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("Socket created.");

                while (true) {
                    System.out.println("Listening to a connection on the local port "
                            + serverSocket.getLocalPort() + "...");
                    Socket client = serverSocket.accept();
                    System.out.println("\nA connection established with the remote port "
                            + client.getPort() + " at "
                            + client.getInetAddress().toString());
                    executeCommand(client);
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
                    //line = in.readLine();
                    StringTokenizer tokenizer = new StringTokenizer(line, " ");
                    if (!tokenizer.hasMoreTokens()) {
                        break;
                    }

                    if (tokenizer.nextToken().equals("GET")) {

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
        try {
            DataInputStream fin = new DataInputStream(new FileInputStream(file));
            try {

                out.println("HTTP/1.0 200 OK");
                String fileName = file.getName();
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                if (fileExt.equals("htm") || fileExt.equals("html")) {
                    out.println("Content-Type: text/html");
                } else if (fileExt.equals("png")) {
                    out.println("Content-Type: image/png");
                }else if (fileExt.equals("jpeg") || fileExt.equals("jpg")) {
                    out.println("Content-Type: image/jpeg");
                }

                int len = (int) file.length();
                out.println("Content-Length: " + len);
                out.println("");

                byte buf[] = new byte[len];
                fin.readFully(buf);
                out.write(buf, 0, len);
                out.flush();
            } finally {
                fin.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TinyHttpd2 server = new TinyHttpd2();
        server.init();
    }

}
