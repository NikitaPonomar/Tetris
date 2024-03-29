package com.example.tetris.datamodel;

import java.util.Collections;
import java.util.LinkedList;

public class Three extends Figure {

    public Three(String[][] figureBody, int positionY, int positionX, boolean spin,LinkedList<Figure> history) {
        super(figureBody, positionY, positionX, spin, history);
    }


    public static Figure createFigure() {

        String [][] threeFigure=new String[][] {{"1", "1", "1"}};
        int positionY=-1;
        int positionX=3;
        boolean spin=false;
        LinkedList<Figure> history =new LinkedList<>(Collections.emptyList());
        return new Figure(threeFigure,positionY,positionX, spin, history);

    }
}
