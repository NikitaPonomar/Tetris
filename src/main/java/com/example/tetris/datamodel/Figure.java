package com.example.tetris.datamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Figure {
    public String[][] figureBody;
    public int positionY;
    public int positionX;
    boolean spin;
    public LinkedList<Figure> history;
    public static final int FIELD_WIDTH = 10;

    public Figure(String[][] figureBody, int positionY, int positionX, boolean spin, LinkedList<Figure> history) {
        this.figureBody = figureBody;
        this.positionY = positionY;
        this.positionX = positionX;
        this.spin = spin;
        this.history = history;
    }

    public boolean isSpin() {
        return spin;
    }

    public void setSpin(boolean spin) {
        this.spin = spin;
    }

    public Figure(Figure myFigure) {
        this.figureBody = myFigure.getFigureBody();
        this.positionY = myFigure.getPositionY();
        this.positionX = myFigure.getPositionX();
        this.spin = myFigure.isSpin();
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

    public static Figure createFigure() {
        String[][] figureBody = {{"1"}};
        int positionY = -1;
        int positionX = 3;
        boolean spin = false;
        LinkedList<Figure> history = new LinkedList<>(Collections.emptyList());
        return new Figure(figureBody, positionY, positionX, spin, history);

    }

    public Figure rotateFigure() {
        String[][] newFigureBody = new String[figureBody[0].length][figureBody.length];
        if (!spin) {
            for (int i = 0; i < figureBody.length; i++) {
                for (int j = 0; j < figureBody[0].length; j++) {
                    newFigureBody[j][i] = figureBody[i][j];
                }
            }
            //    return new Figure(newFigureBody,positionY,positionX,true,history);
        } else {
            for (int i = 0; i < figureBody.length; i++) {
                for (int j = 0; j < figureBody[0].length; j++) {
                    newFigureBody[figureBody[0].length - 1 - j][i] = figureBody[i][j];
                }
            }
        }
        int deltaX = 0;
        if (figureBody[0].length > 2) {
            deltaX = 1;
        } else {
            deltaX = -1;
            if (positionX == 0) deltaX = 0;
        }
        if (positionX + deltaX + figureBody.length >= FIELD_WIDTH) {
            deltaX = FIELD_WIDTH - positionX - figureBody.length;
        }

        int deltaY = 0;
        if (positionY + deltaY - figureBody[0].length < 0) {
            deltaY = 0 - positionY + figureBody[0].length - 2;
        }

        LinkedList<Figure> tmpHistory = new LinkedList<>(Collections.emptyList());
        //   tmpHistory.add(new Figure(figureBody,positionY,positionX,spin,new LinkedList<>(Collections.emptyList())));
        return new Figure(newFigureBody, positionY + deltaY, positionX + deltaX, !spin, tmpHistory);
    }

    @Override
    public String toString() {
        return "Figure{" +
                "figureBody=" + Arrays.deepToString(figureBody) +
                ", positionY=" + positionY +
                ", positionX=" + positionX +
                ", spin=" + spin +
                '}';
    }
}
