package com.example.tetris;

import com.example.tetris.datamodel.GameField;
import com.example.tetris.datamodel.HorizontalLine;
import com.example.tetris.datamodel.ThreadCenter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


public class TetrisController {
    @FXML
    private TableView<HorizontalLine> tableView;

    @FXML
    TableColumn<HorizontalLine, String> tetrisCol1, tetrisCol2, tetrisCol3, tetrisCol4, tetrisCol5, tetrisCol6, tetrisCol7, tetrisCol8, tetrisCol9, tetrisCol10;

    @FXML
    public void initialize() {

        //binding our ObservableList with TableView
        tableView.setItems(GameField.getInstance().getField());


        tetrisCol1.setCellValueFactory(p -> {
            String formattedValue = p.getValue().col1Property().get()
                    .toUpperCase();
            return new SimpleStringProperty(formattedValue);
        });

        tetrisCol2.setCellValueFactory(p -> p.getValue().col2Property());
        tetrisCol3.setCellValueFactory(p -> p.getValue().col3Property());
        tetrisCol4.setCellValueFactory(p -> p.getValue().col4Property());
        tetrisCol5.setCellValueFactory(p -> p.getValue().col5Property());
        tetrisCol6.setCellValueFactory(p -> p.getValue().col6Property());
        tetrisCol7.setCellValueFactory(p -> p.getValue().col7Property());
        tetrisCol8.setCellValueFactory(p -> p.getValue().col8Property());
        tetrisCol9.setCellValueFactory(p -> p.getValue().col9Property());
        tetrisCol10.setCellValueFactory(p -> p.getValue().col10Property());

        tetrisCol1.setCellFactory(col -> {
            TableCell<HorizontalLine, String> cell = new TableCell<>();

            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Node graphic = createPriorityGraphic(newValue);
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
                }
            });
            return cell;
        });

        tetrisCol2.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol3.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol4.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol5.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol6.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol7.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol8.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol9.setCellFactory(tetrisCol1.getCellFactory());
        tetrisCol10.setCellFactory(tetrisCol1.getCellFactory());

        tableView.setSelectionModel(null);


    }


    @FXML
    public void handleOnTableKeyPressed(KeyEvent keyEvent) {
        String receivedCommand = keyEvent.getCode().toString();
        System.out.println(receivedCommand);
        GameField.getInstance().handleKeyPressed(receivedCommand);

    }

    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tableView.getScene().getWindow());
        alert.setTitle("Exit");
        alert.setHeaderText("Exit from GAME without saving");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) System.exit(0);
    }

    @FXML
    private void handleStoreToFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        try {
            File file = chooser.showSaveDialog(tableView.getScene().getWindow());
            if (file == null) return;
            GameField.getInstance().storeToFile(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleLoadFromFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Resource File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File file = null;
        try {
            file = chooser.showOpenDialog(tableView.getScene().getWindow());
            if (file == null) return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Alert.AlertType loadResult = GameField.getInstance().loadFromFile(file);
        Alert alert = new Alert(loadResult);
        if (loadResult == Alert.AlertType.WARNING) {
            alert.setHeaderText("No new available contacts in file:\n" + file.getPath());
        } else {
            alert.setHeaderText("Game was loaded successfully!");
        }
        Optional<ButtonType> opt = alert.showAndWait();

    }

    @FXML
    private Node createPriorityGraphic(String value) {
        HBox graphicContainer = new HBox();
        graphicContainer.setAlignment(Pos.CENTER_LEFT);
        if (!value.equals("0") && value != "") {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/figure.png")));
            imageView.setFitHeight(25);
            imageView.setPreserveRatio(true);
            graphicContainer.getChildren().add(imageView);
        }
        return graphicContainer;
    }

    @FXML
    private void launchGame() {


        Thread myThreads = new Thread(new ThreadCenter());
        myThreads.start();




        System.out.println("finish");


    }

}