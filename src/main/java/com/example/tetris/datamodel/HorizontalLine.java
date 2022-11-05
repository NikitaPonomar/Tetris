package com.example.tetris.datamodel;

import javafx.beans.property.SimpleStringProperty;

public class HorizontalLine implements Comparable<HorizontalLine> {
    private SimpleStringProperty col1,col2,col3,col4,col5,col6,col7,col8,col9,col10;

    public HorizontalLine(String col1, String col2, String col3, String col4,String col5,String col6,String col7,String col8,String col9,String col10) {
        // checking if property is null, if it is true, setting it as empty string
        setCol1(col1);
        setCol2(col2);
        setCol3(col3);
        setCol4(col4);
        setCol5(col5);
        setCol6(col6);
        setCol7(col7);
        setCol8(col8);
        setCol9(col9);
        setCol10(col10);
    }

    public HorizontalLine(HorizontalLine horizontalLine) {
        // checking if property is null, if it is true, setting it as empty string
        setCol1(horizontalLine.getCol1());
        setCol2(horizontalLine.getCol2());
        setCol3(horizontalLine.getCol3());
        setCol4(horizontalLine.getCol4());
        setCol5(horizontalLine.getCol5());
        setCol6(horizontalLine.getCol6());
        setCol7(horizontalLine.getCol7());
        setCol8(horizontalLine.getCol8());
        setCol9(horizontalLine.getCol9());
        setCol10(horizontalLine.getCol10());
    }

    public SimpleStringProperty col1Property() {
        if (col1 == null) col1 = new SimpleStringProperty(this, "col1");
        return col1;
    }

    public SimpleStringProperty col2Property() {
        if (col2 == null) col2 = new SimpleStringProperty(this, "col2");
        return col2;
    }

    public SimpleStringProperty col3Property() {
        if (col3 == null) col3 = new SimpleStringProperty(this, "col3");
        return col3;
    }

    public SimpleStringProperty col4Property() {
        if (col4 == null) col4 = new SimpleStringProperty(this, "col4");
        return col4;
    }

    public SimpleStringProperty col5Property() {
        if (col5 == null) col5 = new SimpleStringProperty(this, "col5");
        return col5;
    }

    public SimpleStringProperty col6Property() {
        if (col6 == null) col6 = new SimpleStringProperty(this, "col6");
        return col6;
    }

    public SimpleStringProperty col7Property() {
        if (col7 == null) col7 = new SimpleStringProperty(this, "col7");
        return col7;
    }

    public SimpleStringProperty col8Property() {
        if (col8 == null) col8 = new SimpleStringProperty(this, "col8");
        return col8;
    }

    public SimpleStringProperty col9Property() {
        if (col9 == null) col9 = new SimpleStringProperty(this, "col9");
        return col9;
    }

    public SimpleStringProperty col10Property() {
        if (col10 == null) col10 = new SimpleStringProperty(this, "col10");
        return col10;
    }
    public String getCol1() {
        return col1Property().get();
    }
    public String getCol2() {
        return col2Property().get();
    }
    public String getCol3() {
        return col3Property().get();
    }
    public String getCol4() {
        return col4Property().get();
    }
    public String getCol5() {
        return col5Property().get();
    }
    public String getCol6() {
        return col6Property().get();
    }
    public String getCol7() {
        return col7Property().get();
    }
    public String getCol8() {
        return col8Property().get();
    }
    public String getCol9() {
        return col9Property().get();
    }
    public String getCol10() {
        return col10Property().get();
    }

    public static HorizontalLine getTripleLine(){
        return new  HorizontalLine("0","0","0","1","1","1","0","0","0","0");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HorizontalLine that = (HorizontalLine) o;

        if (!this.getCol1().equals(that.getCol1())) return false;
        if (!this.getCol2().equals(that.getCol2())) return false;
        if (!this.getCol3().equals(that.getCol3())) return false;
        if (!this.getCol4().equals(that.getCol4())) return false;
        if (!this.getCol5().equals(that.getCol5())) return false;
        if (!this.getCol6().equals(that.getCol6())) return false;
        if (!this.getCol7().equals(that.getCol7())) return false;
        if (!this.getCol8().equals(that.getCol8())) return false;
        if (!this.getCol9().equals(that.getCol9())) return false;
        return this.getCol10().equals(that.getCol10());
    }

    @Override
    public int hashCode() {
        int result = col1 != null ? col1.hashCode() : 0;
        result = result + (col2 != null ? col2.hashCode() : 0);
        result = result + (col3 != null ? col3.hashCode() : 0);
        result = result + (col4 != null ? col4.hashCode() : 0);
        result = result + (col5 != null ? col5.hashCode() : 0);
        result = result + (col6 != null ? col6.hashCode() : 0);
        result = result + (col7 != null ? col7.hashCode() : 0);
        result = result + (col8 != null ? col8.hashCode() : 0);
        result = result + (col9 != null ? col9.hashCode() : 0);
        result = result + (col10 != null ? col10.hashCode() : 0);
        return result;
    }



    @Override
    public int compareTo(HorizontalLine o) {
        return this.getCol2().compareTo(o.getCol2());
    }

    public void setCol1(String col1) {
        col1Property().set(col1);
    }
    public void setCol2(String col2) {
        col2Property().set(col2);
    }
    public void setCol3(String col3) {
        col3Property().set(col3);
    }
    public void setCol4(String col4) {
        col4Property().set(col4);
    }
    public void setCol5(String col5) {
        col5Property().set(col5);
    }
    public void setCol6(String col6) {
        col6Property().set(col6);
    }
    public void setCol7(String col7) {
        col7Property().set(col7);
    }
    public void setCol8(String col8) {
        col8Property().set(col8);
    }
    public void setCol9(String col9) {
        col9Property().set(col9);
    }
    public void setCol10(String col10) {
        col10Property().set(col10);
    }

    @Override
    public String toString() {
//        return "HorizontalLine{" +
//                "col1=" + getCol1() +
//                ", col2=" + getCol2() +
//                ", col3=" + getCol3() +
//                ", col4=" + getCol4() +
//                ", col5=" + getCol5() +
//                ", col6=" + getCol6() +
//                ", col7=" + getCol7() +
//                ", col8=" + getCol8() +
//                ", col9=" + getCol9() +
//                ", col10=" + getCol10() +
//                '}';
        return ""+getCol1() + getCol2() + getCol3() + getCol4() + getCol5() + getCol6() + getCol7() + getCol8() + getCol9() + getCol10();

    }
}
