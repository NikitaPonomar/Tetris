module com.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens com.example.tetris to javafx.fxml, javafx.base;
    exports com.example.tetris;
}