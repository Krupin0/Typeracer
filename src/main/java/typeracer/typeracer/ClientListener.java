package typeracer.typeracer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread {
    Socket ss;
    ProgressBar pb1;
    ProgressBar pb2;
    Label countDown;
    TextFlow text;
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
            if(temp.startsWith("start")){
                    String finalTemp = temp;
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            countDown.setText(finalTemp.charAt(5)+"");
                        }
                    });
            } else if(temp.startsWith("text")) {
                String finalTemp1 = temp;
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        countDown.setVisible(false);
                        HelloController.predloha = finalTemp1.substring(5);
                        try {
                            HelloController.initHelpVar();
                            for(char znak:HelloController.znaky) {
                                Text temp = new Text(String.valueOf(znak));
                                temp.setStyle("-fx-font-size: 20");
                                text.getChildren().add(temp);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } else if(!temp.startsWith("start") && !temp.startsWith("text")){
                String[] oneInfo = temp.split("#");

                for (int i = 0; i < oneInfo.length; i++) {

                        if(Integer.parseInt(oneInfo[i].split(":")[1]) == HelloController.spravnaSlova){
                            pb1.setProgress(Double.parseDouble(oneInfo[i].split(":")[1])/HelloController.pocetSlov);
                            if(pb1.getProgress() == 1){
                                    Platform.runLater(new Runnable() {
                                        @Override public void run() {
                                            countDown.setVisible(true);
                                            countDown.setText("Vyhrál jsi.");
                                        }
                                    });
                            }
                        }
                        else{
                            pb2.setProgress(Double.parseDouble(oneInfo[i].split(":")[1])/HelloController.pocetSlov);
                            if(pb2.getProgress() == 1){
                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                        countDown.setVisible(true);
                                        countDown.setText("Prohrál jsi.");
                                    }
                                });
                            }
                        }
                    }
            }
        }
    }

    public ClientListener(Socket ss, ProgressBar pb1, ProgressBar pb2, Label coundown, TextFlow text) {
        this.ss = ss;
        this.pb1 = pb1;
        this.pb2 = pb2;
        this.countDown = coundown;
        this.text = text;
    }
}