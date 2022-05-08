package typeracer.typeracer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread {
    private Socket ss;
    private ProgressBar pb1;
    private ProgressBar pb2;
    private Label countDown;
    private TextFlow text;
    private boolean konec;
    @Override
    public void run() {

        //Vytvoření readeru
        BufferedReader in = null; //
        try {
            in = new BufferedReader(new InputStreamReader(this.ss.getInputStream()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        konec = false;
        //Příjem a zpracování zpráv
        while (!konec) {
            String temp = null;
            try {
                temp = in.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            if(temp.startsWith("start")){ //Zpráva o startu
                    String finalTemp = temp;
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            countDown.setText(finalTemp.charAt(5)+"");
                        }
                    });
            } else if(temp.startsWith("text")) { //Zpráva o vygenerovaném textu
                String finalTemp1 = temp;
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        countDown.setVisible(false);
                        HelloController.predloha = finalTemp1.substring(5);
                        try { //Vypsání textu
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
            } else{ //Zprávy o skóre, měnění progress baru
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
                                konec = true;
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
                                konec = true;
                            }
                        }
                    }
            }
        }
        try {
            Client.sendMessage("konec");
            ss.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
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