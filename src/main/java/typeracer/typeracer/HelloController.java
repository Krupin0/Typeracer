package typeracer.typeracer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


public class HelloController implements Initializable {

    @FXML
    private TextFlow text;

    @FXML
    private TextArea textInput;
    private final String predloha = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin mattis lacinia justo. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Quisque porta. Integer malesuada. Curabitur ligula sapien, pulvinar a vestibulum quis, facilisis vel sapien. Nullam faucibus mi quis velit. Etiam neque.";
    private char[] znaky = predloha.toCharArray();
    private ArrayList<String> spravneZnaky = new ArrayList<>();
    private ArrayList<String> tempSpravneZnaky = new ArrayList<>();
    private Client cl;

    @FXML
    private void napsatText() throws IOException {

        for(char znak:znaky) {
            Text temp = new Text(String.valueOf(znak));
            temp.setStyle("-fx-font-size: 20");
            text.getChildren().add(temp);
        }

    }

    private void upravitTextSpravne(){
        text.getChildren().clear();
        for (int i = 0; i < spravneZnaky.size(); i++) {
            Text temp = new Text(spravneZnaky.get(i));
            temp.setStyle("-fx-font-size: 20;");
            temp.setFill(Color.web("#99cc00"));
            text.getChildren().add(temp);
        }
        for (int i = 0; i < tempSpravneZnaky.size(); i++) {
            Text temp = new Text(tempSpravneZnaky.get(i));
            temp.setStyle("-fx-font-size: 20;");
            temp.setFill(Color.web("#99cc00"));
            text.getChildren().add(temp);
        }
        for (int i = tempSpravneZnaky.size(); i < znaky.length; i++) {
                Text temp = new Text(String.valueOf(znaky[i]));
                temp.setStyle("-fx-font-size: 20;");
                text.getChildren().add(temp);
        }
    }
    private void upravitTextSpatne(int pocetSpatnych){
        text.getChildren().clear();
        for (int i = 0; i < spravneZnaky.size(); i++) {
            Text temp = new Text(spravneZnaky.get(i));
            temp.setStyle("-fx-font-size: 20;");
            temp.setFill(Color.web("#99cc00"));
            text.getChildren().add(temp);
        }
        for (int i = 0; i < tempSpravneZnaky.size(); i++) {
            Text temp = new Text(tempSpravneZnaky.get(i));
            temp.setStyle("-fx-font-size: 20;");
            temp.setFill(Color.web("#99cc00"));
            text.getChildren().add(temp);
        }
        for (int i = tempSpravneZnaky.size(); i < znaky.length; i++) {
            if(i< (pocetSpatnych + tempSpravneZnaky.size())){
                Text temp = new Text(String.valueOf(znaky[i]));
                temp.setStyle("-fx-font-size: 20");
                temp.setFill(Color.web("#803333"));
                text.getChildren().add(temp);
            }
            else{
                Text temp = new Text(String.valueOf(znaky[i]));
                temp.setStyle("-fx-font-size: 20");
                text.getChildren().add(temp);
            }
        }
    }
    private String getCastPredlohy(int delka){
        String vysledek = "";
        for (int i = 0; i < delka; i++) {
            vysledek = vysledek + znaky[i];
        }
        return vysledek;
    }

    private void odebratSlovo(int delka){
        for (int j = 0; j < delka; j++) {
           znaky = Arrays.copyOfRange(znaky, 1, znaky.length);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {

                if(s2.equals(getCastPredlohy(s2.length()))){
                    if(s2.endsWith(" ")){
                        System.out.println("konec slova");
                        odebratSlovo(s2.length());
                        Platform.runLater(() -> {
                            textInput.setText("");
                        });
                        spravneZnaky.addAll(tempSpravneZnaky);
                        spravneZnaky.add(" ");
                    }
                    //System.out.println("ok");
                    tempSpravneZnaky.clear();
                    for (char znak:s2.toCharArray()) {
                        tempSpravneZnaky.add(znak+"");
                    }
                    textInput.getStyleClass().remove("spatne");
                    System.out.println(textInput.getStyleClass());
                    upravitTextSpravne();
                }
                else{
                    tempSpravneZnaky.clear();
                    int i =0;
                    while(s2.charAt(i) == getCastPredlohy(s2.length()).charAt(i)){
                        tempSpravneZnaky.add(s2.charAt(i)+"");
                        i++;
                    }
                        if(!textInput.getStyleClass().contains("spatne")){
                            textInput.getStyleClass().add("spatne");
                        }

                    upravitTextSpatne(s2.length()-tempSpravneZnaky.size());
                    //System.out.println("spatne");
                }

                System.out.println(spravneZnaky);
                System.out.println(tempSpravneZnaky);


                System.out.println();
            }
        });
    }
}

