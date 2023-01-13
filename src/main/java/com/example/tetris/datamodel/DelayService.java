package com.example.tetris.datamodel;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class DelayService extends Service<Integer> {

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {
            @Override
            protected void succeeded() {
                super.succeeded();
                System.out.println("HELLO FROM SUCCEDED!");
            }

            @Override
            public void run() {
                super.run();
                System.out.println("hello from RUN!");
            }

            @Override
            protected Integer call() throws Exception {
                int speed=GameField.getInstance().getSpeed();

                try {
                    Thread.sleep(speed);
                } catch (InterruptedException ignored) {
                    if (isCancelled()) {
                        updateMessage("Cancelled");
                    }
                }
                System.out.println("service started");
                return speed;
            }
        };
    }
}
