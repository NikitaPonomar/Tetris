package com.example.tetris;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class TetrisApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TetrisApplication.class.getResource("tetris.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 605);
        stage.setTitle("Tetris game");
        stage.setScene(scene);
        stage.show();
        Platform.setImplicitExit(false);

    }


    public static void main(String[] args) {
        launch();
    }
}

