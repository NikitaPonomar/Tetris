package com.example.tetris.datamodel;

import com.example.tetris.TetrisController;

import java.util.Timer;
import java.util.TimerTask;

public class Triple extends Thread {

    // написать Трипл без таймер таска и вынести опускание вниз в отдельный синхронизировнный метод.
    // Вместо period использовать Thread.sleep(). И вызывать interrupt() метод в GameField по щелчку на кнопке DOWN
    private int globalTimer = -1;
    private int limitTimer = 19;
    public Timer timer = new Timer();

    public Triple() {
        HorizontalLine emptyLine = GameField.getInstance().getEmptyLine();
        GameField.getInstance().setTripleLine(HorizontalLine.getTripleLine()); //setting up absolutely new Triple figure in our game field


        timer.schedule(new TimerTask() {
            HorizontalLine previousLine = null;
            HorizontalLine currentLine = null;
            HorizontalLine myFigure = GameField.getInstance().getTripleLine();

            @Override
            public void run() {
//               if(GameField.getInstance().getPresentLineNumber()>globalTimer && globalTimer!=-1){
//                   globalTimer=GameField.getInstance().getPresentLineNumber(); //to input DOWN command from GameField
//               }
                globalTimer++;
                // DO YOUR CODE HERE
         //       GameField.getInstance().setSpeed(400);
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

          //      setGlobalTimer(GameField.getInstance().getPresentLineNumber());

                if (globalTimer >= limitTimer) {
                    timer.cancel();
                    TetrisController.myThreads.interrupt(); // returning control to ThreadCenter

                }
            }
        }, 10, 400);
    }

    public int getGlobalTimer() {
        return globalTimer;
    }

    public void setGlobalTimer(int globalTimer) {this.globalTimer = globalTimer; }

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


