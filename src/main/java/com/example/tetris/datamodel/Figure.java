package com.example.tetris.datamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Figure {
    public String [][] figureBody;
    public int positionY;
    public int positionX;
    public  LinkedList<Figure> history;

    public Figure(String[][] figureBody, int positionY, int positionX, LinkedList<Figure> history) {
        this.figureBody = figureBody;
        this.positionY = positionY;
        this.positionX = positionX;
        this.history = history;
    }

    public Figure(Figure myFigure) {
        this.figureBody = myFigure.getFigureBody();
        this.positionY = myFigure.getPositionY();
        this.positionX = myFigure.getPositionX();
        this.history = new LinkedList<>(Collections.emptyList());

    }

    public String[][] getFigureBody() {
        return figureBody;
    }

    public void setFigureBody(String[][] figureBody) {
        this.figureBody = figureBody;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public LinkedList<Figure> getHistory() {
        return history;
    }

    public void setHistory(LinkedList<Figure> history) {
        this.history = history;
    }

    public static Figure createFigure(){
        String [][] figureBody= {{"1"}};
        int positionY=-1;
        int positionX=3;
        LinkedList<Figure> history =new LinkedList<>(Collections.emptyList());
        return new Figure(figureBody,positionY,positionX,history);

    }

    @Override
    public String toString() {
        return "Figure{" +
                "figureBody=" + Arrays.deepToString(figureBody) +
                ", positionY=" + positionY +
                ", positionX=" + positionX +
                ", history=" + history +
                '}';
    }
}
