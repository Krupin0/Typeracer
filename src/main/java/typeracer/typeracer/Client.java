package typeracer.typeracer;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client {

    private PrintWriter os;
    private ProgressBar pb1;
    private ProgressBar pb2;

    private Label countdown;
    public Client(ProgressBar pb1, ProgressBar pb2, Label countdown, TextFlow text) throws IOException {
        this.pb1 = pb1;
        this.pb2 = pb2;
        this.countdown = countdown;
        InetAddress ia = InetAddress.getByName("127.0.0.1");
        Socket s = new Socket(ia, 55555);
        new ClientListener(s, pb1, pb2, countdown, text).start();
        this.os = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

        //System.out.println("Write messages for the server; \"STOP\" to end  connection");
        //os.close();
        //s.close();
    }

    public void sendMessage(String message){
        os.println(message);
        os.flush();
    }
}