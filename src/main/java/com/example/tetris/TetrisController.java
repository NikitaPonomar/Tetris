package com.example.tetris;

import com.example.tetris.datamodel.DelayService;
import com.example.tetris.datamodel.Figure;
import com.example.tetris.datamodel.GameField;
import com.example.tetris.datamodel.Three;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


public class TetrisController {
    public Service<Integer> delayService;

    public static final int START_POSITION = 0;
    public static final int END_POSITION = 19;

    public Figure movingFigure = generateNextFigure();
    public volatile boolean keyEventDelivered = true;

    @FXML
    private TableView<String[]> table = new TableView<>();
//    @FXML
//    TableColumn<String [], String> tetrisCol1, tetrisCol2, tetrisCol3, tetrisCol4, tetrisCol5, tetrisCol6, tetrisCol7, tetrisCol8, tetrisCol9, tetrisCol10;

    public EventHandler<WorkerStateEvent> succeededCancelledHandler;

    @FXML
    public void initialize() {


        //binding our ObservableList with TableView
        GameField.getInstance().initiateEmptyField();

        for (int i = 0; i < GameField.getInstance().getData().get(0).length; i++) {
            TableColumn tc = new TableColumn();
//            TableColumn tc = new TableColumn(GameField.getInstance().getData().get(0)[i]);
            tc.setSortable(false);


            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setCellFactory(col -> {
                TableCell<String[], String> cell = new TableCell<>();

                cell.itemProperty().addListener((observableValue, o, newValue) -> {
                    if (newValue != null) {
                        Node graphic = createPriorityGraphic(newValue);
                        cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
                    }
                });
                return cell;
            });

            table.getColumns().add(tc);
        }


//        tetrisCol1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String [], String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<String [], String> p) {
//                return new SimpleStringProperty(p.getValue()[0]);
//            }
//        });
//        tetrisCol2.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[1]));
//        tetrisCol3.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[2]));
//        tetrisCol4.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[3]));
//        tetrisCol5.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[4]));
//        tetrisCol6.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[5]));
//        tetrisCol7.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[6]));
//        tetrisCol8.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[7]));
//        tetrisCol9.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[8]));
//        tetrisCol10.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[9]));
//
//        tetrisCol1.setCellFactory(col -> {
//            TableCell<String [], String> cell = new TableCell<>();
//
//            cell.itemProperty().addListener((observableValue, o, newValue) -> {
//                if (newValue != null) {
//                    Node graphic = createPriorityGraphic(newValue);
//                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
//                }
//            });
//            return cell;
//        });
//
//        tetrisCol2.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol3.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol4.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol5.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol6.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol7.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol8.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol9.setCellFactory(tetrisCol1.getCellFactory());
//        tetrisCol10.setCellFactory(tetrisCol1.getCellFactory());


        // making Headers of table hidden
        table.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // Get the table header
                Pane header = (Pane) table.lookup("TableHeaderRow");
                if (header != null && header.isVisible()) {
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                    header.setManaged(false);
                }
            }
        });

        table.setItems(GameField.getInstance().getData());

        table.setSelectionModel(null);

        //      table.setMaxSize(315.0, 532.0);
        table.setMaxSize(315.0, 502.0);

        delayService = new DelayService();


        succeededCancelledHandler = new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                while (!keyEventDelivered) {
                    Thread.onSpinWait();
                }
                System.out.println("Service executed, positionY = " + movingFigure.getPositionY() +
                        " positionX = " + movingFigure.getPositionX());
                boolean success = GameField.getInstance().tryInsertToNextLine(movingFigure);
                if (success) {
                    System.out.println("success, position  " + movingFigure.getPositionY());
                        delayService.reset();
                        delayService.start();
                } else {
                    System.out.println("unsuccess, position" + movingFigure.getPositionY());
                    movingFigure = generateNextFigure();
                        delayService.reset();
                        delayService.start();
                    return;
                }
                if (movingFigure.getPositionY() > END_POSITION) {
                    movingFigure = generateNextFigure();
                }
            }
        };

        delayService.setOnSucceeded(succeededCancelledHandler);
        delayService.setOnCancelled(succeededCancelledHandler);
    }


    @FXML
    public void handleOnTableKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.LEFT) {
            keyEventDelivered = false;   // using this variable to stop move down  during handleOnTableKeyPressed operation
        }
        String receivedCommand = keyEvent.getCode().toString();
        System.out.println(receivedCommand);
        //      if (movingFigure.getPositionY() > START_POSITION) {

        switch (receivedCommand) {
            case "DOWN":
                // speed up the figure falling dawn

                //fireEvent(table,new WorkerStateEvent(delayService, WorkerStateEvent.WORKER_STATE_SUCCEEDED));
                //new WorkerStateEvent(delayService, WorkerStateEvent.WORKER_STATE_SUCCEEDED);
                delayService.cancel();
                break;
            case "RIGHT":
            case "LEFT":
//                        EventHandler<WorkerStateEvent> succeededCancelledHandler = delayService.getOnCancelled();
//                        delayService.removeEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED,succeededCancelledHandler);
//                        delayService.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,succeededCancelledHandler);
            {
                int step;
                if (receivedCommand.contentEquals("RIGHT")) {
                    step = 1;
                } else {
                    step = -1;
                }

                boolean success = GameField.getInstance().tryMoveRightLeft(movingFigure, step);
                if (success) {
                    System.out.println("success, positionX " + movingFigure.getPositionX() +
                            " positionY " + movingFigure.getPositionY());
                } else {
                    System.out.println("unsuccess, positionX " + movingFigure.getPositionX() +
                            " positionY " + movingFigure.getPositionY());
                }
//                            delayService.setOnSucceeded(succeededCancelledHandler);
//                            delayService.cancel();
//                            delayService.setOnCancelled(succeededCancelledHandler);
//                            delayService.reset();
//                            delayService.start();

                keyEventDelivered = true;
            }
            break;


            default:
                return;
        }

        //     }


    }

    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(table.getScene().getWindow());
        alert.setTitle("Exit");
        alert.setHeaderText("Exit from GAME without saving");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    private void handleStoreToFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML files", "*.xml"));
//        keyEventDelivered=false;

 //       isDialog = true;
delayService.setOnSucceeded(null);
delayService.setOnCancelled(null);

        try {
            File file = chooser.showSaveDialog(table.getScene().getWindow());
            if (file == null) return;
            GameField.getInstance().storeToFile(file,movingFigure);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
//        isDialog = false;
        delayService.setOnSucceeded(succeededCancelledHandler);
        delayService.setOnCancelled(succeededCancelledHandler);
        delayService.reset();
        delayService.start();
    }

    @FXML
    private void handleLoadFromFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Resource File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File file = null;
    //    isDialog = true;
        delayService.setOnSucceeded(null);
        delayService.setOnCancelled(null);

        try {
            //    keyEventDelivered=false;
            file = chooser.showOpenDialog(table.getScene().getWindow());
            if (file == null) return;
        } catch (Exception e) {
            e.printStackTrace();
            //    keyEventDelivered=true;
            return;
        }


        Alert.AlertType loadResult = GameField.getInstance().loadFromFile(file);
        Alert alert = new Alert(loadResult);
        if (loadResult == Alert.AlertType.WARNING) {
            alert.setHeaderText("No new available contacts in file:\n" + file.getPath());
        } else if (loadResult == Alert.AlertType.WARNING) {
            alert.setHeaderText("Incorrect file:\n" + file.getPath());
        } else {
            alert.setHeaderText("Game was loaded successfully!");
        }
        Optional<ButtonType> opt = alert.showAndWait();
  //      isDialog = false;
        delayService.setOnSucceeded(succeededCancelledHandler);
        delayService.setOnCancelled(succeededCancelledHandler);
        delayService.reset();
        delayService.start();
    }


    @FXML
    private Node createPriorityGraphic(String value) {
        if (!value.equals("0") && value != "") {
            Rectangle graphic = new Rectangle();
            graphic.setHeight(25);
            graphic.setWidth(25);
            graphic.setOpacity(80);
            return graphic;
        }
        return null;
    }

    @FXML
    private void launchGame() {
        movingFigure = generateNextFigure();
        GameField.getInstance().initiateEmptyField();

        if (delayService.getState() == Service.State.SUCCEEDED || delayService.getState() == Service.State.CANCELLED) {
            delayService.reset();
            delayService.start();
        } else if (delayService.getState() == Service.State.READY) {
            delayService.start();

        }

    }

    public Figure generateNextFigure() {
        return Three.createFigure();
    }

}