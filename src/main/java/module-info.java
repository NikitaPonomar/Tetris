module com.example.contactbook {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.contactbook to javafx.fxml;
    exports com.example.contactbook;
}