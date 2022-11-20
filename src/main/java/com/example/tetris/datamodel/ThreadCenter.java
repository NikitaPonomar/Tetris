package com.example.tetris.datamodel;

public class ThreadCenter implements Runnable {
    Figure horizontalFigure=null;

    public Figure getHorizontalFigure() {return horizontalFigure;}

    @Override
    public void run() {
        do {
            horizontalFigure = new Trio();
            horizontalFigure.start();
            System.out.println(horizontalFigure.getName() + "thread name");
            try {
                horizontalFigure.join(10000);
            } catch (InterruptedException e) {
                System.out.println("Triple thread has been interrupted by InterruptedException");
                continue;
            }
            GameField.getInstance().calcHorizontalLine();
        } while (true);
    }


}
