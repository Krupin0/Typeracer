module typeracer.typeracer {
    requires javafx.controls;
    requires javafx.fxml;


    opens typeracer.typeracer to javafx.fxml;
    exports typeracer.typeracer;
}