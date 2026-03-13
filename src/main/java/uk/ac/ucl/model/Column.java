package uk.ac.ucl.model;

import java.util.ArrayList;

public class Column {
    private String name;
    private ArrayList rows;

    public Column(String name){
        this.name = name;
        this.rows = new ArrayList();
    }

    public void addRowValue(String value){
        this.rows.add(value);
    }

    public void setRowValue(int index, String value){
        this.rows.set(index, value);
    }

    public String getName(){
        return this.name;
    }

    public int getSize(){
        return this.rows.size();
    }

    public String getRowValue(int index){
        return this.rows.get(index).toString();
    }

    public void removeRow(int index){
        this.rows.remove(index);
    }
}
