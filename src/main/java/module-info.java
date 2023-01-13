module com.example.contactbook {
    requires javafx.controls;
    requires javafx.fxml;



    opens com.example.tetris to javafx.fxml;
    exports com.example.tetris;
}