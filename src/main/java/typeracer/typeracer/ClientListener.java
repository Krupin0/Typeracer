package typeracer.typeracer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread {
    Socket ss;

    @Override
    public void run() {
        BufferedReader in = null; // vytvoření BufferReaderu, se vstupním proudem se socketu klienta
        try {
            in = new BufferedReader(new InputStreamReader(this.ss.getInputStream()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // nekonečný while cyklus pro vypisování zpráv od serveru
        while (true) {
            String temp = null; // načtení zprávy od klienta do proměnné temp
            try {
                temp = in.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(temp); // vypsání zprávy klienta z proměnné
        }
    }

    public ClientListener(Socket ss) {
        this.ss = ss;
    }
}