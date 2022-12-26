package com.example.tetris.datamodel;

public class ThreadCenter implements Runnable {
    Figure horizontalFigure=null;
    public boolean isActive;


    public ThreadCenter() {
        isActive = true;
    }

    public Figure getHorizontalFigure() {return horizontalFigure;}

    @Override
    public void run() {
        while (isActive){

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
        }
    }

    public void disable(){

        isActive=false;
    }
}
