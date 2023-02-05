package com.example.tetris.datamodel;

import java.util.Collections;
import java.util.LinkedList;

public class Stud extends Figure{
    public Stud (String[][] figureBody, int positionY, int positionX, boolean spin, LinkedList<Figure> history) {
        super(figureBody, positionY, positionX, spin, history);
    }

    public static Figure createFigure() {
        String [][] figureBody=new String[][] {{"0", "1","0"},{"1","1", "1"}};
        int positionY=0;
        int positionX=3;
        boolean spin=false;
        LinkedList<Figure> history =new LinkedList<>(Collections.emptyList());
        return new Figure(figureBody,positionY,positionX, spin, history);
    }
}
