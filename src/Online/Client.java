package Online;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String [] args) {
        String serverName = "localhost";
        int port = 6066;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            Scanner sc = new Scanner(System.in);
            while(true) {
                String inString = "";
                if(in.available() != 0) {
                    inString = in.readUTF();
                }
                if(inString.contains("|n")) {
                    out.writeUTF(sc.next());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

