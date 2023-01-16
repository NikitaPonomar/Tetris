package com.example.tetris.datamodel;

import java.util.LinkedList;

public class Figure {
    protected String [][] figureBody;
    protected int positionY;
    protected int positionX;
    protected LinkedList<Figure> history;

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
        this.history = new LinkedList<>(myFigure.getHistory());
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
        int positionY=0;
        int positionX=3;
        LinkedList<Figure> history =new LinkedList<>();
        return new Figure(figureBody,positionY,positionX,history);

    }
}
