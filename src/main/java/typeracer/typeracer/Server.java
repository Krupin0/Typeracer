package typeracer.typeracer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<PrintWriter> writers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Server is running.");

        ServerSocket serverSocket = new ServerSocket(55555);

        while (true){
            System.out.println("Čekám na klienty");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nový klient!");
            writers.add(new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            new ClientConnHandle(clientSocket).start();
        }
    }
    public static void sendToAll(String text){
        for (PrintWriter writer:writers) {
            //System.out.println(writer);
            writer.println("Od serveru: " + text);
            writer.flush();
        }
    }
}