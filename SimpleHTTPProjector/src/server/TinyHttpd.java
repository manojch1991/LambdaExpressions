package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import pool.RunningThread;
import pool.StaticThreadPool;

public class TinyHttpd {

    private static final int PORT = 8888;
    private ServerSocket serverSocket;
    Socket client;
    private Date startDate;

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
                    StaticThreadPool staticThreadPool = StaticThreadPool.getInstance(5, true);

                    staticThreadPool.execute(new RunningThread(this));

                }
            } finally {
                serverSocket.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void execute() {
        try {
            HttpHandler handler = new HttpHandler(client, serverSocket, startDate);
            handler.executeCommand();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        TinyHttpd server = new TinyHttpd();
        server.init();
    }
}
