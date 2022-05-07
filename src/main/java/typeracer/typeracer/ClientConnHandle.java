package typeracer.typeracer;

import java.io.*;
import java.net.Socket;

public class ClientConnHandle extends Thread{

    private Socket clientSocket;

    public ClientConnHandle(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        BufferedReader is = null;
        PrintWriter os = null;
        try {
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String message;
        try {
            while (!(message = is.readLine()).isEmpty()) {
                Server.sendToAll(Thread.currentThread().getName() + ":" + message);
            }
        }catch (IOException e){
            System.out.println(e);
        }
        try {
            os.close();
            is.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
