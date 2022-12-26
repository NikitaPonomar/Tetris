package com.example.tetris.datamodel;

public abstract class Figure extends Thread {
     int globalTimer = -1;
     int limitTimer = 19;
     int delay=400;
    HorizontalLine emptyLine = GameField.getInstance().getEmptyLine();

    private boolean isActive;

    public void disable(){ isActive=false; }

    public Figure() {
        isActive = true;
    }

    @Override
    public synchronized void run() {
        while(isActive && globalTimer < limitTimer) {

            globalTimer++;
            // DO YOUR CODE HERE

            moveDown(); // may be it is better to send current Figure as object to moveDown class
                try {
                    System.out.println("current speed " + GameField.getInstance().getSpeed());
                    GameField.getInstance().setCurrentFigureThread(currentThread());
                    Thread.sleep(GameField.getInstance().getSpeed());
                } catch (InterruptedException e) {
                    System.out.println(" wake up this thread! " + currentThread().getName());
                }
 //           GameField.getInstance().setSpeed(delay); //setting usual speed for next loop

        }
    }

    public abstract void moveDown();

    public int getDelay() {return delay;}

    public void setDelay(int delay) {this.delay = delay;}
}
