package com.example.tetris.datamodel;

import com.example.tetris.TetrisController;

import java.util.Timer;
import java.util.TimerTask;

public class Triple extends Thread {
    private int globalTimer = -1;
    private int limitTimer = 19;


    public Triple() {
        HorizontalLine emptyLine = GameField.getInstance().getEmptyLine();
        GameField.getInstance().setTripleLine(HorizontalLine.getTripleLine()); //setting up absolutely new Triple figure in our game field

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            HorizontalLine previousLine = null;
            HorizontalLine currentLine = null;
            HorizontalLine mergedLine = null;
            HorizontalLine myFigure = GameField.getInstance().getTripleLine();

            @Override
            public void run() {
                globalTimer++;
                // DO YOUR CODE HERE
                myFigure = GameField.getInstance().getTripleLine();
                GameField.getInstance().setPresentLineNumber(globalTimer);

                if (globalTimer > 0) {
                    if (previousLine == null) {
                        GameField.getInstance().getField().set(globalTimer - 1, emptyLine);
                        previousLine = new HorizontalLine(emptyLine);
                    } else {
                        GameField.getInstance().getField().set(globalTimer - 1, previousLine);
                    }

                }


                if (GameField.getInstance().getField().get(globalTimer).equals(emptyLine)) {
                    //           System.out.println(field.get(globalTimer));

                    GameField.getInstance().getField().set(globalTimer, myFigure);

                } else {
                    //trying to inject our figure in current line
                    //     mergedLine=currentLine;
                    currentLine = GameField.getInstance().getField().get(globalTimer);
                    String stringCurrentLine = currentLine.toString();
                    String stringMyFigure = myFigure.toString();
                    int valueIndex = stringMyFigure.indexOf("111");
                    if (stringCurrentLine.substring(valueIndex, valueIndex + 3).contentEquals("000")) {
                        //we found place for our figure
                        previousLine = new HorizontalLine(currentLine);
                        addFigureToLine(globalTimer,myFigure,currentLine);
                    } else  {
 //                       GameField.getInstance().getField().set(globalTimer-1,myFigure);
                        currentLine = new HorizontalLine(GameField.getInstance().getField().get(globalTimer-1)) ;
                        addFigureToLine(globalTimer-1,myFigure,currentLine);
                        timer.cancel();
                        TetrisController.myThreads.interrupt(); // returning control to ThreadCenter


                    }

                }

                if (globalTimer == limitTimer) {
                    timer.cancel();
                    TetrisController.myThreads.interrupt();


                }
            }
        }, 10, 400);
    }

    public int getGlobalTimer() {
        return globalTimer;
    }

    public static void addFigureToLine (int position, HorizontalLine myFigure, HorizontalLine currentLine){
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
        GameField.getInstance().getField().set(position, currentLine);
    }

}


