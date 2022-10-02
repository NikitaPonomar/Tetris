package com.example.contactbook;

import com.example.contactbook.datamodel.ContactData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//import jfxtras.styles.jmetro.JMetro;
//import jfxtras.styles.jmetro.Style;
public class ContactApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ContactApplication.class.getResource("contact-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 240);
        stage.setTitle("My Contacts!");
        //        JMetro jMetro = new JMetro(Style.DARK);
//        jMetro.setScene(scene);
        //       AquaFx.style();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}