package com.hw;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println ("Attemping to connect to host on port 8888.");
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;
        try{
            echoSocket = new Socket("localhost", 8888);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
	        stdIn = new BufferedReader(new InputStreamReader(System.in));
	        String userInput;
	        System.out.print ("input: ");
	        while ((userInput = stdIn.readLine()) != null) {
				System.out.println("Enter 'exit' to exit");
			    out.println(userInput);
			    System.out.println("Your input: " + in.readLine());
		        System.out.print ("input: ");
	        }
        }catch (IOException e) {
        	System.out.println(e);
        } finally{
        	out.close();
    		echoSocket.close();
        }
    }
}
