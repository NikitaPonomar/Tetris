package com.example.tetris.datamodel;

public  class Trio extends Figure {
    HorizontalLine previousLine = null;
    HorizontalLine currentLine = null;
     public HorizontalLine myFigure = HorizontalLine.getTripleLine();

    public Trio(){
        GameField.getInstance().setTripleLine(myFigure);//setting up absolutely new Triple figure in our game field
    }

    @Override
    public void moveDown() {
  //      synchronized (GameField.getInstance().getField()){
            if (GameField.getInstance().getField().isEmpty()) return;
            myFigure = GameField.getInstance().getTripleLine();
            GameField.getInstance().setPresentLineNumber(globalTimer);
            System.out.println(globalTimer);
            if (GameField.getInstance().getField().get(globalTimer).equals(emptyLine)) {
                GameField.getInstance().getField().set(globalTimer, myFigure);
            } else  {
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
                    if (GameField.getInstance().getField().isEmpty()) return;
                    currentLine = new HorizontalLine(GameField.getInstance().getField().get(globalTimer-1)) ;
                    addFigureToLine(globalTimer-1,myFigure,currentLine);
                    disable(); // Figure thread finished and  return control to ThreadCenter
                }
            }
            if (globalTimer > 0) {
                if (previousLine == null) {
                    GameField.getInstance().getField().set(globalTimer - 1, emptyLine);
                    previousLine = new HorizontalLine(emptyLine);
                } else {
                    GameField.getInstance().getField().set(globalTimer - 1, previousLine);
                }
            }
     //   }





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
