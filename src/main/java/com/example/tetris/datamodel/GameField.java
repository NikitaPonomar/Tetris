package com.example.tetris.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Iterator;

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
        int lineNumber=GameField.getInstance().getPresentLineNumber();
        HorizontalLine currentLine=GameField.getInstance().getField().get(lineNumber);
        String stringCurrentLine=currentLine.toString();
        String stringMyFigure=GameField.getInstance().getTripleLine().toString();

              switch (receivedCommand) {
                case "RIGHT":
                    System.out.println(stringMyFigure);
                    int index=stringMyFigure.indexOf("1110");
                    if (index>-1 && (index+4)<=stringCurrentLine.length() && stringCurrentLine.substring(index,index+4).contentEquals("1110")){
                        stringMyFigure= stringMyFigure.replace("1110","0111");
                        System.out.println("new figure"+stringMyFigure);
                    }
                    break;

                case "LEFT":
                    int index2=stringMyFigure.indexOf("0111");
                    if (index2>-1 && stringCurrentLine.substring(index2,index2+4).contentEquals("0111")) {
                        stringMyFigure = stringMyFigure.replace("0111", "1110");
                    }
                    break;
                default:
                    return;
            }

            String[] myStrings = stringMyFigure.split("");
            HorizontalLine myFigure = new HorizontalLine(myStrings[0], myStrings[1], myStrings[2], myStrings[3], myStrings[4], myStrings[5], myStrings[6], myStrings[7], myStrings[8], myStrings[9]);
            GameField.getInstance().setTripleLine(myFigure);


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
