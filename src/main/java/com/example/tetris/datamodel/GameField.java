package com.example.tetris.datamodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class GameField {
    private static GameField instance = new GameField();
    //   private  ObservableList<HorizontalLine> field = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    String[][] staffArray = new String[20][10];
    public ObservableList<String[]> data = FXCollections.observableArrayList();
    public static final int FIELD_SIZE = 20;
    public static final int FIELD_WIDTH = 10;
    public IntegerProperty score = new SimpleIntegerProperty(0);
    private int speed = 400;

    private int presentLineNumber = 0;

    private GameField() {

    }


    private static class SingletonHolder {
        public static final GameField HOLDER_INSTANCE = new GameField();
    }


    public void calcHorizontalLine() {
        for (int i = FIELD_SIZE - 1; i >= 0; i--) {
            int sum = 0;
            for (int j = FIELD_WIDTH - 1; j >= 0; j--) {
                if (data.get(i)[j].contentEquals("1")) sum++;
            }
            if (sum == 10) {
                score.set(score.get()+1);
             //   score++;
                System.out.println(" your score: " + score);
            //    data.set(i, new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
                data.remove(i);
                data.add(0,new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
            }
        }
    }

    public List<String[]> deleteFigureFromCopyOfField (Figure figureToRemove){
        List<String[]> tmpList = new ArrayList<>(data);
        int removePositionY = figureToRemove.getPositionY();
        int removePositionX = figureToRemove.getPositionX();
        for (int i = figureToRemove.getFigureBody().length - 1; i >= 0; i--) {
            for (int j = figureToRemove.getFigureBody()[i].length - 1; j >= 0; j--) {
                if (figureToRemove.getFigureBody()[i][j].contentEquals("1")) {
                    tmpList.get(removePositionY - (figureToRemove.getFigureBody().length - 1 - i))[removePositionX + j] = "0";
                }
            }
        }
        return tmpList;
    }

    public void storeToFile(File file, Figure figureToRemove) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<soap:Envelope xmlns:soap= \"https://www.svoy-med.ru/ContactBook-soap\">\n" +
                    "<soap:body>\n");
            bw.write("<score>" + score.get() + "</score>\n");
            bw.write("<speed>" + speed + "</speed>\n");
            bw.write("<field>\n");

            //delete movingFugure from copy of Field
            List<String[]> tmpList = deleteFigureFromCopyOfField(figureToRemove);


            //saving field to file
            for (int i = 0; i < tmpList.size(); i++) {
                bw.write(Arrays.deepToString(tmpList.get(i)));
                bw.newLine();
            }
            bw.write("</field>\n");
            bw.write("</soap:body>\n" +
                    "</soap:Envelope>");
            bw.newLine();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public Alert.AlertType loadFromFile(File file) {

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                stringBuilder.append(br.readLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String commonString = new String(stringBuilder);
        if (!commonString.contains("0")) {
            return Alert.AlertType.WARNING;
        }


        score = new SimpleIntegerProperty( Integer.parseInt(parseStringValue(commonString, "<score>", "</score>")));;
        speed = Integer.parseInt(parseStringValue(commonString, "<speed>", "</speed>"));
        commonString=parseStringValue(commonString, "<field>", "</field>");

        List<String> parsingFieldList = List.of(commonString.split("]"));

        if (parsingFieldList.size() != FIELD_SIZE) {
            System.out.println("incorrect size of file");
            return Alert.AlertType.WARNING;
        }

        data.clear();
        for (int i = 0; i < FIELD_SIZE; i++) {
            String[] tmp = parsingFieldList.get(i).replace("[", "").split(", ");
            data.add(tmp);
        }

        Alert.AlertType processResult = Alert.AlertType.INFORMATION;
        return processResult;

    }

    private String parseStringValue(String str, String firstTag, String secondTag) {
        if (str.contains(firstTag) && str.contains(secondTag)) {
            int start = str.lastIndexOf(firstTag) + firstTag.length();
            int end = str.lastIndexOf(secondTag);
            if (end <= start) return "0";
            else return str.substring(start, end);
        }
        return "0";
    }

    public String[][] getStaffArray() {
        return staffArray;
    }

    public void initiateEmptyField() {
        data.clear();
        for (int i = 0; i < 20; i++) {
//            for (int j = 0; j < 10; j++) {
//                staffArray[i][j] = "0";
//            }
            data.add(new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"});
        }

        //     data.addAll(Arrays.asList(staffArray));

    }

    public void clearData() {
        data.clear();
    }

    public static GameField getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public ObservableList<String[]> getData() {
        return data;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean tryInsertToNextLine(Figure myFigure) {
        if (!data.isEmpty()) {
            String[][] figureBody = myFigure.getFigureBody();
            myFigure.setPositionY(myFigure.getPositionY() + 1);
            int positionY = myFigure.getPositionY();
            int positionX = myFigure.getPositionX();
            LinkedList<Figure> history = myFigure.getHistory();

            //checking if any place to insert figure in the field
            {
                if (positionY >= FIELD_SIZE) return false;
                int i = figureBody.length - 1;
                for (int j = figureBody[i].length - 1; j >= 0; j--) {
                    if (figureBody[i][j].contentEquals("1") &&
                            data.get(positionY - (figureBody.length - 1 - i))[positionX + j].contentEquals("1")) {
                        System.out.println("no place in next line to insert figure");

                        return false;
                    }
                }
            }

            //removing previous figure from field
            removePreviousFigure(history);

            // inserting new figure to field
            for (int i = figureBody.length - 1; i >= 0; i--) {
                for (int j = figureBody[i].length - 1; j >= 0; j--) {
                    if (figureBody[i][j].contentEquals("1") &&
                            data.get(positionY - (figureBody.length - 1 - i))[positionX + j].contentEquals("0")) {
                        data.get(positionY - (figureBody.length - 1 - i))[positionX + j] = "1";
                    } else {
                  //      throw new IllegalArgumentException("field changed during inserting the figure");

                    }
                }
                //creating new Array to call Listener of ObservableValue
                String[] tmp = data.get(positionY - (figureBody.length - 1 - i)).clone();

                data.set(positionY - (figureBody.length - 1 - i), tmp);
            }

            myFigure.getHistory().add(new Figure(myFigure));
            calcHorizontalLine();
                        System.out.println(myFigure);
                        System.out.println("Field = ");
                        for (int l=0;l< data.size();l++){
                                System.out.println(Arrays.deepToString(data.get(l)));
                        }
            return true;
        }
        System.out.println("field is empty for unknown reason");
        return false;
    }

    public boolean tryMoveRightLeft(Figure movingFigure, int step) {
        if (!data.isEmpty()) {
            String[][] figureBody = movingFigure.getFigureBody();
            int positionY = movingFigure.getPositionY();
            int positionX = movingFigure.getPositionX();
            LinkedList<Figure> history = movingFigure.getHistory();

            //checking if any place to move figure to right/left in the field
            {
                if (positionY- figureBody.length+1 < 0)  {
                    System.out.println("Next Figure has not in field yet");
                    return false;
                }
                int j;
                if (step > 0) {
                    if ((positionX + figureBody[0].length) >= FIELD_WIDTH) return false;
                    j = figureBody[0].length - 1;
                } else {
                    if (positionX == 0) return false;
                    j = 0;
                }

                for (int i = figureBody.length - 1; i >= 0; i--) {
                    if (figureBody[i][j].contentEquals("1") &&
                            data.get(positionY - (figureBody.length - 1 - i))[positionX + j + step].contentEquals("1")) {
                        System.out.println("no place to Right/Left " + step + " to insert figure");
                        return false;
                    }
                }
            }

            //removing previous figure from field
            removePreviousFigure(history);

            // inserting new figure to field
            for (int i = figureBody.length - 1; i >= 0; i--) {
                for (int j = figureBody[i].length - 1; j >= 0; j--) {
                    if (figureBody[i][j].contentEquals("1") &&
                            data.get(positionY - (figureBody.length - 1 - i))[positionX + j + step].contentEquals("0")) {
                        data.get(positionY - (figureBody.length - 1 - i))[positionX + j + step] = "1";
                    } else {
                //        throw new IllegalArgumentException("field changed during inserting the figure");
                    }
                }
                //creating new Array to call Listener of ObservableValue
                String[] tmp = data.get(positionY - (figureBody.length - 1 - i)).clone();

                data.set(positionY - (figureBody.length - 1 - i), tmp);
            }

            movingFigure.setPositionX(positionX + step);
            movingFigure.getHistory().add(new Figure(movingFigure));
            return true;
        }
        System.out.println("field is empty for unknown reason");
        return false;
    }

    public boolean tryInsertTurnedFigureToField(Figure movingFigure) {
        if (!data.isEmpty()) {
            String[][] figureBody = movingFigure.getFigureBody();
            int positionY = movingFigure.getPositionY();
            int positionX = movingFigure.getPositionX();
            boolean spin=movingFigure.isSpin();
            LinkedList<Figure> history = movingFigure.getHistory();

            //checking if it is possible to turn figure in the field
                if (positionY- figureBody.length+1 < 0) {
                    System.out.println("Next Figure has not in field yet");
                    return false;
                }

                //1. delete movingFigure from copy of Field
                List<String[]> tmpList = deleteFigureFromCopyOfField(movingFigure);

                //2. Checking the place in this new cleaned up field

                Figure tmpFigure=movingFigure. rotateFigure();
                if (tmpFigure.getPositionY()-tmpFigure.getFigureBody().length+1<0) return false;

                for (int i=tmpFigure.getFigureBody().length-1;i>=0;i--){
                    for (int j=tmpFigure.getFigureBody()[0].length-1; j>=0; j--) {
                        if (tmpFigure.getFigureBody()[i][j].contentEquals("1") &&
                                tmpList.get(tmpFigure.getPositionY() - (tmpFigure.getFigureBody().length - 1 - i))[tmpFigure.getPositionX() + j].contentEquals("1")){
                            System.out.println("no place to turn the Figure");
                            return false;
                        }
                    }
                }



            //removing previous figure from field
            removePreviousFigure(history);

            // inserting new tmpFigure to field
            for (int i = tmpFigure.getFigureBody().length - 1; i >= 0; i--) {
                for (int j =tmpFigure.getFigureBody()[i].length - 1; j >= 0; j--) {
                    if (tmpFigure.getFigureBody()[i][j].contentEquals("1") &&
                            data.get(tmpFigure.getPositionY() - (tmpFigure.getFigureBody().length - 1 - i))[tmpFigure.getPositionX() + j].contentEquals("0")) {
                        data.get(tmpFigure.getPositionY() - (tmpFigure.getFigureBody().length - 1 - i))[tmpFigure.getPositionX() + j] = "1";
                    } else {
                   //     throw new IllegalArgumentException("field changed during inserting the figure");
                    }
                }
                //creating new Array to call Listener of ObservableValue
                String[] tmp = data.get(tmpFigure.getPositionY() - (tmpFigure.getFigureBody().length - 1 - i)).clone();

                data.set(tmpFigure.getPositionY() - (tmpFigure.getFigureBody().length- 1 - i), tmp);
            }

            movingFigure.setFigureBody(tmpFigure.getFigureBody());
            movingFigure.setPositionY(tmpFigure.getPositionY());
            movingFigure.setPositionX(tmpFigure.getPositionX());
            movingFigure.setSpin(tmpFigure.isSpin());
            movingFigure.getHistory().add(new Figure(movingFigure));

            return true;
        }
        System.out.println("field is empty for unknown reason");
        return false;
    }

    private void removePreviousFigure(LinkedList<Figure> history) {
        if (!history.isEmpty()) {
            String[][] previousFigureBody = history.getLast().getFigureBody();
            int previousY = history.getLast().getPositionY();
            int previousX = history.getLast().getPositionX();
            for (int i = previousFigureBody.length - 1; i >= 0; i--) {
                for (int j = previousFigureBody[i].length - 1; j >= 0; j--) {
                    if (previousFigureBody[i][j].contentEquals("1")) {
                        data.get(previousY - (previousFigureBody.length - 1 - i))[previousX + j] = "0";
                    }
                }
            }
        }
    }


}
