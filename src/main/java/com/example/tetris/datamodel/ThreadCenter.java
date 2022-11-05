package com.example.tetris.datamodel;

public class ThreadCenter implements Runnable{
    @Override
    public void run() {
        while (true){
             Triple presentTriple = new Triple();
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                System.out.println("Triple thread has been interrupted by InterruptedException");
                throw new RuntimeException(e);
            }
            GameField.getInstance().calcHorizontalLine();
        }




    }
}
