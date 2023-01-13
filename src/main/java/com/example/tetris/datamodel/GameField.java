package com.example.tetris.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameField {
    private static GameField instance = new GameField();
    private  ObservableList<HorizontalLine> field = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    public static final int FIELD_SIZE=20;

    private int score = 0;
    private int speed = 400;
    Thread currentFigureThread = null;

    private int presentLineNumber = 0;
    private HorizontalLine tripleLine = HorizontalLine.getTripleLine();
    private HorizontalLine emptyLine = new HorizontalLine("0", "0", "0", "0", "0", "0", "0", "0", "0", "0");

    private GameField() {
    }

    private static class SingletonHolder {
        public static final GameField HOLDER_INSTANCE=new GameField();
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


    public void storeToFile(File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<soap:Envelope xmlns:soap= \"https://www.svoy-med.ru/ContactBook-soap\">\n" +
                    "<soap:body>");

            for (int i=0;i< field.size();i++){
                HorizontalLine line = field.get(i);
                // cleaning our field from the moving figure to proper store field
                if (i==getPresentLineNumber()) {
                    line =new HorizontalLine(line);
                    line =removeFigureFromLine(getTripleLine(),line);
                }
                    bw.write("<HorizontalLine>\n" +
                            "    <col1>" + line.getCol1() + "</col1>\n" +
                            "    <col2>" + line.getCol2() + "</col2>\n" +
                            "    <col3>" + line.getCol3() + "</col3>\n" +
                            "    <col4>" + line.getCol4() + "</col4>\n" +
                            "    <col5>" + line.getCol5() + "</col5>\n" +
                            "    <col6>" + line.getCol6() + "</col6>\n" +
                            "    <col7>" + line.getCol7() + "</col7>\n" +
                            "    <col8>" + line.getCol8() + "</col8>\n" +
                            "    <col9>" + line.getCol9() + "</col9>\n" +
                            "    <col10>" + line.getCol4() + "</col10>\n" +
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

        List <HorizontalLine> tmpList=new ArrayList<>();
        for (String str : parsingContactList) {
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
            HorizontalLine newLine = new HorizontalLine(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);
            tmpList.add(newLine);
        }
        if (tmpList.size()!=FIELD_SIZE) {
            System.out.println("incorrect size of file");
            return Alert.AlertType.WARNING;
        }
        if (field.isEmpty()){
            for (int i=0;i<tmpList.size();i++){
            field.add(tmpList.get(i));
            }
        } else {
            for (int i=0;i<tmpList.size();i++){
                field.set(i,tmpList.get(i));
            }
        }
        processResult = Alert.AlertType.INFORMATION;
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

    public void initiateEmptyField() {
        for (int i=0;i<FIELD_SIZE;i++){
            this.field.add(emptyLine);
        }
    }
    public void clearField() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            field.set(i, new HorizontalLine(emptyLine));
        }
    }

    public HorizontalLine removeFigureFromLine ( HorizontalLine figureToRemove, HorizontalLine currentLine){
        if (figureToRemove.getCol1().contentEquals("1")) currentLine.setCol1("0");
        if (figureToRemove.getCol2().contentEquals("1")) currentLine.setCol2("0");
        if (figureToRemove.getCol3().contentEquals("1")) currentLine.setCol3("0");
        if (figureToRemove.getCol4().contentEquals("1")) currentLine.setCol4("0");
        if (figureToRemove.getCol5().contentEquals("1")) currentLine.setCol5("0");
        if (figureToRemove.getCol6().contentEquals("1")) currentLine.setCol6("0");
        if (figureToRemove.getCol7().contentEquals("1")) currentLine.setCol7("0");
        if (figureToRemove.getCol8().contentEquals("1")) currentLine.setCol8("0");
        if (figureToRemove.getCol9().contentEquals("1")) currentLine.setCol9("0");
        if (figureToRemove.getCol10().contentEquals("1")) currentLine.setCol10("0");
        return currentLine;
    }

    public static GameField getInstance() { return SingletonHolder.HOLDER_INSTANCE; }
    public ObservableList<HorizontalLine> getField() { return field; }
    public HorizontalLine getTripleLine() { return tripleLine; }
    public void setTripleLine(HorizontalLine tripleLine) { this.tripleLine = tripleLine; }
    public HorizontalLine getEmptyLine() { return emptyLine; }
    public int getPresentLineNumber() {return presentLineNumber;}
    public void setPresentLineNumber(int presentLineNumber) { this.presentLineNumber = presentLineNumber;}
    public int getSpeed() { return speed;}
    public void setSpeed(int speed) { this.speed = speed;}
    public Thread getCurrentFigureThread() { return currentFigureThread; }
    public void setCurrentFigureThread(Thread currentFigureThread) { this.currentFigureThread = currentFigureThread; }

    public boolean tryInsertToField(int position, HorizontalLine myFigure){
        if (!field.isEmpty()){
            System.out.println(Thread.currentThread().getName());
            HorizontalLine currentLine = field.get(position);
            String stringCurrentLine =currentLine.toString();
            String stringMyFigure = myFigure.toString();
            for (int i=0;i<10;i++){
                if(stringMyFigure.charAt(i) == '1' && stringCurrentLine.charAt(i) == '1') {
                    return false;
                }
            }

            if (myFigure.getCol1().contentEquals("1")) currentLine.setCol1("1");
            if (myFigure.getCol2().contentEquals("1")) currentLine.setCol2("1");
            if (myFigure.getCol3().contentEquals("1")) currentLine.setCol3("1");
            if (myFigure.getCol4().contentEquals("1")) currentLine.setCol4("1");
            if (myFigure.getCol5().contentEquals("1")) currentLine.setCol5("1");
            if (myFigure.getCol6().contentEquals("1")) currentLine.setCol6("1");
            if (myFigure.getCol7().contentEquals("1")) currentLine.setCol7("1");
            if (myFigure.getCol8().contentEquals("1")) currentLine.setCol8("1");
            if (myFigure.getCol9().contentEquals("1")) currentLine.setCol9("1");
            if (myFigure.getCol10().contentEquals("1")) currentLine.setCol10("1");
            field.set(position,currentLine);
            return true;
        }
        System.out.println("field is empty for unknown reason");
        return false;
    }
    public boolean tryInsertToField(int position, HorizontalLine oldFigure, HorizontalLine newFigure){
        if (!field.isEmpty()){
            removeFigureFromLine(oldFigure,field.get(position));
            if (tryInsertToField(position,newFigure)){
                return true;
            } else{
                if (!tryInsertToField(position,oldFigure)) throw new RuntimeException("exception during moving figure to RIGHT/LEFT");
                return false;
            }

        }
        System.out.println("field is empty for unknown reason");
        return false;
    }

//    public boolean  tryInsertToField(int position, HorizontalLine oldFigure, HorizontalLine newFigure){
//        if (!field.isEmpty()){
//            System.out.println("oldFigure = " + oldFigure + "newFigure = "+newFigure);
//            String stringOldFigure=oldFigure.toString();
//            String oldBody =stringOldFigure.replaceAll("0","");
//            int indexOld = stringOldFigure.indexOf(oldBody);
//            int lengthOld=oldBody.length();
//            HorizontalLine lineBeforeMoving=field.get(position);
//            String stringLineBeforeMoving=lineBeforeMoving.toString();
//            String newLine;
//            if (indexOld==0){
//                 newLine= "0".repeat(lengthOld)+stringLineBeforeMoving.substring(lengthOld);
//            } else {
//                 newLine= stringLineBeforeMoving.substring(0,indexOld)+"0".repeat(lengthOld)+stringLineBeforeMoving.substring(indexOld+lengthOld);
//            }
//
//            String stringNewFigure=newFigure.toString();
//            String newBody =stringNewFigure.replaceAll("0","");
//            int indexNew = stringNewFigure.indexOf(newBody);
//            int lengthNew=newBody.length();
//            if (newLine.substring(indexNew,indexNew+lengthNew).contentEquals("0".repeat(lengthNew))){
//                if (indexNew==0){
//                    newLine= newBody+newLine.substring(lengthNew);
//                    field.set(position,new HorizontalLine(newLine.split("")));
//                    return true;
//                } else {
//                    newLine= newLine.substring(0,indexNew)+newBody+newLine.substring(indexNew+lengthNew);
//                    field.set(position,new HorizontalLine(newLine.split("")));
//                    return true;
//                }
//            } else {
//                System.out.println("could not find the place to rotate figure to RIGHT/LEFT");
//                return false;
//            }
//        }
//        System.out.println("field is empty for unknown reason");
//        return false;
//
//    }

   public void cleanPreviousLine (int position, HorizontalLine myFigure) {
       HorizontalLine currentLine = field.get(position);
       if (myFigure.getCol1().contentEquals("1")) currentLine.setCol1("0");
       if (myFigure.getCol2().contentEquals("1")) currentLine.setCol2("0");
       if (myFigure.getCol3().contentEquals("1")) currentLine.setCol3("0");
       if (myFigure.getCol4().contentEquals("1")) currentLine.setCol4("0");
       if (myFigure.getCol5().contentEquals("1")) currentLine.setCol5("0");
       if (myFigure.getCol6().contentEquals("1")) currentLine.setCol6("0");
       if (myFigure.getCol7().contentEquals("1")) currentLine.setCol7("0");
       if (myFigure.getCol8().contentEquals("1")) currentLine.setCol8("0");
       if (myFigure.getCol9().contentEquals("1")) currentLine.setCol9("0");
       if (myFigure.getCol10().contentEquals("1")) currentLine.setCol10("0");

   }
}
