package typeracer.typeracer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client {

    private PrintWriter os;

    public Client() throws IOException {
        InetAddress ia = InetAddress.getByName("127.0.0.1");
        Socket s = new Socket(ia, 55555);
        new ClientListener(s).start();
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