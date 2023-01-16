package com.example.tetris.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class GameField {
    private static GameField instance = new GameField();
    //   private  ObservableList<HorizontalLine> field = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    String[][] staffArray = new String[20][10];
    public ObservableList<ArrayList<String>> data = FXCollections.observableArrayList();
    public static final int FIELD_SIZE = 20;

    private int score = 0;
    private int speed = 400;
    Thread currentFigureThread = null;

    private int presentLineNumber = 0;

    private GameField() {

    }

    private static class SingletonHolder {
        public static final GameField HOLDER_INSTANCE = new GameField();
    }


    public void calcHorizontalLine() {
//        for (int i = field.size() - 1; i > 0; i--) {
//            String currentLine = field.get(i).toString();
//            if (sumFromString(currentLine) == 10) {
//                score++;
//                field.remove(i);
//                field.add(0, emptyLine);
//            }
//        }
    }

    static int sumFromString(String str) {
        int sum = 0;
        for (char ch : str.toCharArray()) {
            if (Character.isDigit(ch)) {
                sum += (ch - '0');
            }
        }
        return sum;
    }


    public void storeToFile(File file) throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
//            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                    "<soap:Envelope xmlns:soap= \"https://www.svoy-med.ru/ContactBook-soap\">\n" +
//                    "<soap:body>");
//
//            for (int i=0;i< field.size();i++){
//                HorizontalLine line = field.get(i);
//                // cleaning our field from the moving figure to proper store field
//                if (i==getPresentLineNumber()) {
//                    line =new HorizontalLine(line);
//                    line =removeFigureFromLine(getTripleLine(),line);
//                }
//                    bw.write("<HorizontalLine>\n" +
//                            "    <col1>" + line.getCol1() + "</col1>\n" +
//                            "    <col2>" + line.getCol2() + "</col2>\n" +
//                            "    <col3>" + line.getCol3() + "</col3>\n" +
//                            "    <col4>" + line.getCol4() + "</col4>\n" +
//                            "    <col5>" + line.getCol5() + "</col5>\n" +
//                            "    <col6>" + line.getCol6() + "</col6>\n" +
//                            "    <col7>" + line.getCol7() + "</col7>\n" +
//                            "    <col8>" + line.getCol8() + "</col8>\n" +
//                            "    <col9>" + line.getCol9() + "</col9>\n" +
//                            "    <col10>" + line.getCol4() + "</col10>\n" +
//                            "</HorizontalLine>");
//                    bw.newLine();
//            }
//
//            bw.write("</soap:body>\n" +
//                    "</soap:Envelope>");
//            bw.newLine();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }

    }

//    public Alert.AlertType loadFromFile(File file) {
//
////        StringBuilder stringBuilder = new StringBuilder();
////        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
////            while (br.ready()) {
////                stringBuilder.append(br.readLine());
////            }
////        } catch (IOException e) {
////            System.out.println(e.getMessage());
////        }
////        String commonString = new String(stringBuilder);
////        if (!commonString.contains("<HorizontalLine>")) {
////            return Alert.AlertType.WARNING;
////        }
////        String[] parsingContactList = new String(stringBuilder).split("<HorizontalLine>");
////        Alert.AlertType processResult = Alert.AlertType.WARNING;
////
////        List <HorizontalLine> tmpList=new ArrayList<>();
////        for (String str : parsingContactList) {
////            if (!str.contains("</HorizontalLine>")) continue;
////            String col1 = parseStringValue(str, "<col1>", "</col1>");
////            String col2 = parseStringValue(str, "<col2>", "</col2>");
////            String col3 = parseStringValue(str, "<col3>", "</col3>");
////            String col4 = parseStringValue(str, "<col4>", "</col4>");
////            String col5 = parseStringValue(str, "<col5>", "</col5>");
////            String col6 = parseStringValue(str, "<col6>", "</col6>");
////            String col7 = parseStringValue(str, "<col7>", "</col7>");
////            String col8 = parseStringValue(str, "<col8>", "</col8>");
////            String col9 = parseStringValue(str, "<col9>", "</col9>");
////            String col10 = parseStringValue(str, "<col10>", "</col10>");
////            HorizontalLine newLine = new HorizontalLine(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);
////            tmpList.add(newLine);
////        }
////        if (tmpList.size()!=FIELD_SIZE) {
////            System.out.println("incorrect size of file");
////            return Alert.AlertType.WARNING;
////        }
////        if (field.isEmpty()){
////            for (int i=0;i<tmpList.size();i++){
////            field.add(tmpList.get(i));
////            }
////        } else {
////            for (int i=0;i<tmpList.size();i++){
////                field.set(i,tmpList.get(i));
////            }
////        }
////        processResult = Alert.AlertType.INFORMATION;
////        return processResult;
//
//    }

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
           data.add(new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "0", "0")));
        }

   //     data.addAll(Arrays.asList(staffArray));

    }

    public void clearData() {
        data.clear();
    }

    public static GameField getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public ObservableList<ArrayList<String>> getData() {
        return data;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Thread getCurrentFigureThread() {
        return currentFigureThread;
    }

    public boolean tryInsertToNextLine(Figure myFigure) {
        if (!data.isEmpty()) {
            String[][] figureBody = myFigure.getFigureBody();
            int positionY = myFigure.getPositionY();
            int positionX = myFigure.getPositionX();
            LinkedList<Figure> history = myFigure.getHistory();


            //checking if any place to insert figure in the field
            {
                int i = figureBody.length - 1;
                for (int j = figureBody[i].length - 1; j >= 0; j--) {
                    if (figureBody[i][j].contentEquals("1") &&
                            data.get(positionY - (figureBody.length - 1 - i)).get(positionX + j).contentEquals("1")) {
                        System.out.println("no place to insert figure");
                        return false;
                    }
                }

            }

            //removing previous figure from field
            if (!history.isEmpty()){
                String[][]  previousFigureBody=history.getLast().getFigureBody();
                int previousY=history.getLast().getPositionY();
                int previousX=history.getLast().getPositionX();
                for (int i=previousFigureBody.length-1;i>=0;i--){
                    for (int j=previousFigureBody[i].length-1;j>=0;j--){
                        if (previousFigureBody[i][j].contentEquals("1")) {
                            data.get(previousY - (previousFigureBody.length - 1 - i)).set(previousX + j,"0");
                        }
                    }
                }
            }


            // inserting new figure to field
                for (int i = figureBody.length - 1; i >= 0; i--) {
                    for (int j = figureBody[i].length - 1; j >= 0; j--) {
                        if (figureBody[i][j].contentEquals("1") &&
                                data.get(positionY - (figureBody.length - 1 - i)).get(positionX +j).contentEquals("0")) {
                            data.get(positionY - (figureBody.length - 1 - i)).set(positionX + j,"1");
                        } else {
                            throw new IllegalArgumentException("field changed during inserting the figure");
                        }
                    }
                    ArrayList<String> tmp=new ArrayList<>(data.get(positionY - (figureBody.length - 1 - i)));
                    data.set(positionY - (figureBody.length - 1 - i),tmp);
                }




            myFigure.getHistory().add(new Figure (myFigure));
            myFigure.setPositionY(positionY+1);

            return true;
        }
        System.out.println("field is empty for unknown reason");
        return false;
    }

}
