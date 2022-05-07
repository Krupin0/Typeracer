package typeracer.typeracer;

import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Server {
    private static ArrayList<PrintWriter> writers = new ArrayList<>();
    private  static TreeMap pocetOdeslanychSlov = new TreeMap();
    private static int pocetPripojeni = 0;
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Server is running.");

        ServerSocket serverSocket = new ServerSocket(55555);

        while (true){
            if(pocetPripojeni<2){
                System.out.println("Čekám na klienty");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nový klient!");
                writers.add(new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                new ClientConnHandle(clientSocket).start();
                pocetPripojeni++;
                if(pocetPripojeni == 2){
                    Thread.sleep(1000);
                    sendToAll("start3");
                    Thread.sleep(1000);
                    sendToAll("start2");
                    Thread.sleep(1000);
                    sendToAll("start1");
                    Thread.sleep(1000);
                    sendToAll("text:" + Server.randomText() + " ");
                }
            }
        }
    }

    private static String randomText() {
        //return "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin mattis lacinia justo. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Quisque porta. Integer malesuada. Curabitur ligula sapien, pulvinar a vestibulum quis, facilisis vel sapien. Nullam faucibus mi quis velit. Etiam neque.";
        return "Krátký text, který je na zkoušku. Dobrý den. Den Dobrý.";
    }

    public static void sendToAll(String text){
        if(text.startsWith("start") || text.startsWith("text")){
            for (PrintWriter writer:writers) {
                writer.println(text);
                writer.flush();
            }
        }
        else{
            String[] separated = text.split(":");
            String celek = "";
            pocetOdeslanychSlov.put(separated[0], separated[1]);
            for (PrintWriter writer:writers) {
                //System.out.println(writer);
                pocetOdeslanychSlov.forEach((key, value) -> {
                    writer.print(key + ":" + value);
                    writer.print("#");
                });
                writer.println("");
                writer.flush();
            }
        }

    }
}