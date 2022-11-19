package com.example.tetris.datamodel;

public class ThreadCenter implements Runnable {




    @Override
    public void run() {
        do {
          Triple   presentTriple = new Triple();

            System.out.println(presentTriple.getName() + "thread name");
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                System.out.println("Triple thread has been interrupted by InterruptedException");
                continue;
            }
            GameField.getInstance().calcHorizontalLine();
        } while (true);
    }


}
