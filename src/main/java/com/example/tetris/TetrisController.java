package com.example.tetris;

import com.example.tetris.datamodel.DelayService;
import com.example.tetris.datamodel.Figure;
import com.example.tetris.datamodel.GameField;
import com.example.tetris.datamodel.Three;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class TetrisController {
    public Service<Integer> delayService;

    public static final int START_POSITION = 0;
    public static final int END_POSITION = 19;

    public Figure movingFigure = generateNextFigure();
    public volatile boolean keyEventDelivered = true;

    @FXML
    private TableView<ArrayList<String>> table = new TableView<>();
    @FXML
    TableColumn<ArrayList<String>, String> tetrisCol1, tetrisCol2, tetrisCol3, tetrisCol4, tetrisCol5, tetrisCol6, tetrisCol7, tetrisCol8, tetrisCol9, tetrisCol10;

    @FXML
    public void initialize() {


        //binding our ObservableList with TableView
        GameField.getInstance().initiateEmptyField();




        tetrisCol1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
                return new SimpleStringProperty(p.getValue().get(0));
            }
        });
        tetrisCol2.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(1)));
        tetrisCol3.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(2)));
        tetrisCol4.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(3)));
        tetrisCol5.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(4)));
        tetrisCol6.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(5)));
        tetrisCol7.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(6)));
        tetrisCol8.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(7)));
        tetrisCol9.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(8)));
        tetrisCol10.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(9)));

        tetrisCol1.setCellFactory(col -> {
            TableCell<ArrayList<String>, String> cell = new TableCell<>();

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
        table.setItems(GameField.getInstance().getData());

        table.setSelectionModel(null);

        table.setMaxSize(315.0, 532.0);

        delayService = new DelayService();


        EventHandler<WorkerStateEvent> succeededCancelledHandler = new EventHandler<WorkerStateEvent>() {
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
             //       ObservableList<String[]> testList = GameField.getInstance().getData();

                    for (int i = 0; i < table.getItems().size(); i++) {
                        ArrayList<String> tmp = table.getItems().get(i);
                        for (int j = 0; j < tmp.size(); j++) {
                            System.out.print(tmp.get(j));
                        }
                        System.out.print("\n");
                    }
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
/*        keyEventDelivered = false;
        String receivedCommand = keyEvent.getCode().toString();
        System.out.println(receivedCommand);
        if (movingFigure.getPositionY() > START_POSITION) {

            switch (receivedCommand) {
                case "DOWN":
                    // speed up the figure falling dawn
                    delayService.cancel();
                    keyEventDelivered = true;
                    break;
                case "RIGHT":
                case "LEFT":
//                        EventHandler<WorkerStateEvent> succeededCancelledHandler = delayService.getOnCancelled();
//                        delayService.setOnCancelled(null);
//                        delayService.setOnSucceeded(null);
                    HorizontalLine oldFigure = movingFigure;
                    movingFigure = movingFigure.toRightLeft(receivedCommand);
                    if (oldFigure.equals(movingFigure)) return;
                    boolean success = GameField.getInstance().tryInsertToNextLine(positionY, oldFigure, movingFigure);
                    if (success) {
                        System.out.println("success, position  " + positionY);
                        if ((positionY - 1) >= START_POSITION) {
                            GameField.getInstance().cleanPreviousLine(positionY - 1, oldFigure);
                        }


                    } else {
                        System.out.println("unsuccess, position" + positionY);
                        movingFigure = oldFigure;
                    }

                    if (positionY >= START_POSITION && positionY < END_POSITION) {
                        positionY++;

//                            delayService.setOnSucceeded(succeededCancelledHandler);
//                            delayService.setOnCancelled(succeededCancelledHandler);
//                            delayService.reset();
//                            delayService.start();

                    }

                    keyEventDelivered = true;

                    break;


                default:
                    keyEventDelivered = true;
                    return;
            }

        }
*/

    }

    @FXML
    private void handleExit() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.initOwner(table.getScene().getWindow());
//        alert.setTitle("Exit");
//        alert.setHeaderText("Exit from GAME without saving");
//        alert.setContentText("Are you sure?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            task.disable();
//            System.exit(0);
//        }
    }

    @FXML
    private void handleStoreToFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        try {
            File file = chooser.showSaveDialog(table.getScene().getWindow());
            if (file == null) return;
            GameField.getInstance().storeToFile(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleLoadFromFile() {
//        FileChooser chooser = new FileChooser();
//        chooser.setTitle("Open Resource File");
//        chooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
//
//        File file = null;
//
//        try {
//            file = chooser.showOpenDialog(table.getScene().getWindow());
//            if (file == null) return;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        if (myThreads.isAlive()) {
//            //cleaning game field and finish figure thread by sending multiple IterationExceptions
//            while (GameField.getInstance().getCurrentFigureThread().isAlive()) {
//                GameField.getInstance().getCurrentFigureThread().interrupt();
//            }
//            GameField.getInstance().getField().clear();
//        }
//
//        Alert.AlertType loadResult = GameField.getInstance().loadFromFile(file);
//        Alert alert = new Alert(loadResult);
//        if (loadResult == Alert.AlertType.WARNING) {
//            alert.setHeaderText("No new available contacts in file:\n" + file.getPath());
//        } else if (loadResult == Alert.AlertType.WARNING) {
//            alert.setHeaderText("Incorrect file:\n" + file.getPath());
//        } else {
//            alert.setHeaderText("Game was loaded successfully!");
//        }
//        Optional<ButtonType> opt = alert.showAndWait();
//        if (!myThreads.isAlive()) myThreads.start();

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