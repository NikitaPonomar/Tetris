package com.example.tetris.datamodel;

public class Three {
    public static volatile HorizontalLine three = new  HorizontalLine("0","0","0","1","1","1","0","0","0","0");


    public static void reset(){
        three.setCol1("0");
        three.setCol2("0");
        three.setCol3("0");
        three.setCol4("1");
        three.setCol5("1");
        three.setCol6("1");
        three.setCol7("0");
        three.setCol8("0");
        three.setCol9("0");
        three.setCol10("0");
    }


}
