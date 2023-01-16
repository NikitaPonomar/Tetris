package com.example.tetris.datamodel;

import java.util.LinkedList;

public class Three extends Figure {

    public Three(String[][] figureBody, int positionY, int positionX, LinkedList<Figure> history) {
        super(figureBody, positionY, positionX, history);
    }


    public static Figure createFigure() {

        String [][] threeFigure=new String[][] {{"1", "1", "1"}};
        int positionY=0;
        int positionX=3;
        LinkedList<Figure> history =new LinkedList<>();
        return new Figure(threeFigure,positionY,positionX,history);

    }
}
