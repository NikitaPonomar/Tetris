package com.example.tetris.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.rotate;

public class GameField {
    private static GameField instance = new GameField();
    private ObservableList<HorizontalLine> field = FXCollections.observableArrayList();
    private int score = 0;
    private int presentLineNumber = 0;
    private HorizontalLine tripleLine = HorizontalLine.getTripleLine();
    private HorizontalLine emptyLine = new HorizontalLine("0", "0", "0", "0", "0", "0", "0", "0", "0", "0");

    private GameField() {
    }

    public void handleKeyPressed (String receivedCommand) {
        HorizontalLine myFigure = GameField.getInstance().getTripleLine();
        int lineNumber=GameField.getInstance().getPresentLineNumber();
 //       HorizontalLine currentLine=GameField.getInstance().getField().get(lineNumber);
        System.out.println(lineNumber);
        String stringCurrentLine=GameField.getInstance().getField().get(lineNumber).toString();
        String stringMyFigure=GameField.getInstance().getTripleLine().toString();
 //       String stringMyFigure=myFigure.toString();
        List<String> myStrings = Arrays.asList(myFigure.getCol1(), myFigure.getCol2(), myFigure.getCol3(), myFigure.getCol4(), myFigure.getCol5(), myFigure.getCol6(), myFigure.getCol7(), myFigure.getCol8(), myFigure.getCol9(), myFigure.getCol10());

        if (stringMyFigure.contentEquals(stringCurrentLine)) {
              switch (receivedCommand) {
                case "RIGHT":
                    System.out.println(stringMyFigure);
                    stringMyFigure= stringMyFigure.replace("1110","0111");
                    System.out.println("new figure"+stringMyFigure);

                    break;
                case "LEFT":
                    stringMyFigure=stringMyFigure.replace("0111","1110");
                    break;
                default:
                    return;
            }

            String[] myStrings2 = stringMyFigure.split("");
            HorizontalLine myFigure2 = new HorizontalLine(myStrings2[0], myStrings2[1], myStrings2[2], myStrings2[3], myStrings2[4], myStrings2[5], myStrings2[6], myStrings2[7], myStrings2[8], myStrings2[9]);
            GameField.getInstance().setTripleLine(myFigure2);
        } else {
            //trying to rotate our figure in current line
            int valueIndex = stringMyFigure.indexOf("111");
//            if (stringCurrentLine.substring(valueIndex,valueIndex+3).contentEquals("000")){
                switch (receivedCommand) {
                    case "RIGHT":
                        if (myStrings.get(myStrings.size() - 1) == "0" && stringCurrentLine.substring(valueIndex+3,valueIndex+4)=="0") {
                            rotate(myStrings, 1);
                        }
                        break;
                    case "LEFT":
                        if (myStrings.get(0) == "0" && stringCurrentLine.substring(valueIndex-1,valueIndex)=="0") {
                            rotate(myStrings, -1);

                        }
                        break;
                    default:
                        return;
                }

                myFigure = new HorizontalLine(myStrings.get(0), myStrings.get(1), myStrings.get(2), myStrings.get(3), myStrings.get(4), myStrings.get(5), myStrings.get(6), myStrings.get(7), myStrings.get(8), myStrings.get(9));
                GameField.getInstance().setTripleLine(myFigure);

        }



    }

    public void calcHorizontalLine() {
        for (int i = field.size() - 1; i > 0; i--) {
            String currentLine = field.get(i).toString();
            if (sumFromString(currentLine) == 10) {
                score++;
                field.remove(i);
                field.add(0, emptyLine);
            }
        }
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

    public boolean addLine(HorizontalLine horizontalLine) {
        field.add(horizontalLine);
        return true;
    }


    public void storeToFile(File file) throws IOException {
        //       Path path = Paths.get(filename);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<soap:Envelope xmlns:soap= \"https://www.svoy-med.ru/ContactBook-soap\">\n" +
                    "<soap:body>");
            Iterator<HorizontalLine> iterator = field.iterator();
            while (iterator.hasNext()) {
                HorizontalLine contact = iterator.next();
                bw.write("<HorizontalLine>\n" +
                        "    <col1>" + contact.getCol1() + "</col1>\n" +
                        "    <col2>" + contact.getCol2() + "</col2>\n" +
                        "    <col3>" + contact.getCol3() + "</col3>\n" +
                        "    <col4>" + contact.getCol4() + "</col4>\n" +
                        "    <col5>" + contact.getCol5() + "</col5>\n" +
                        "    <col6>" + contact.getCol6() + "</col6>\n" +
                        "    <col7>" + contact.getCol7() + "</col7>\n" +
                        "    <col8>" + contact.getCol8() + "</col8>\n" +
                        "    <col9>" + contact.getCol9() + "</col9>\n" +
                        "    <col10>" + contact.getCol4() + "</col10>\n" +
                        "</HorizontalLine>");
                bw.newLine();
            }
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
        if (!commonString.contains("<HorizontalLine>")) {
            return Alert.AlertType.WARNING;
        }
        String[] parsingContactList = new String(stringBuilder).split("<HorizontalLine>");
        Alert.AlertType processResult = Alert.AlertType.WARNING;

        for (String str : parsingContactList) {
            //    System.out.println(str);
            if (!str.contains("</HorizontalLine>")) continue;
            String col1 = parseStringValue(str, "<col1>", "</col1>");
            String col2 = parseStringValue(str, "<col2>", "</col2>");
            String col3 = parseStringValue(str, "<col3>", "</col3>");
            String col4 = parseStringValue(str, "<col4>", "</col4>");
            String col5 = parseStringValue(str, "<col5>", "</col5>");
            String col6 = parseStringValue(str, "<col6>", "</col6>");
            String col7 = parseStringValue(str, "<col7>", "</col7>");
            String col8 = parseStringValue(str, "<col8>", "</col8>");
            String col9 = parseStringValue(str, "<col9>", "</col9>");
            String col10 = parseStringValue(str, "<col10>", "</col10>");

            //          if (col1=="") col1="e";
            HorizontalLine contact = new HorizontalLine(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);
            if (addLine(contact) == true) {
                processResult = Alert.AlertType.INFORMATION;
            }

        }
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


    public static GameField getInstance() {
        return instance;
    }

    public ObservableList<HorizontalLine> getField() {
        return field;
    }

    public HorizontalLine getTripleLine() {
        return tripleLine;
    }

    public void setTripleLine(HorizontalLine tripleLine) {
        this.tripleLine = tripleLine;
    }

    public HorizontalLine getEmptyLine() {
        return emptyLine;
    }

    public void setEmptyLine(HorizontalLine emptyLine) {
        this.emptyLine = emptyLine;
    }

    public int getPresentLineNumber() {
        return presentLineNumber;
    }

    public void setPresentLineNumber(int presentLineNumber) {
        this.presentLineNumber = presentLineNumber;
    }


}
