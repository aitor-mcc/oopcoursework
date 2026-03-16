package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.List;


public class DataFrame {
    private ArrayList<Column> columns;

    public DataFrame(){
        this.columns = new ArrayList<Column>();
    }

    public void addColumn(Column column){
        this.columns.add(column);
    }

    public ArrayList<String> getColumnNames(){
        ArrayList<String> namesList = new ArrayList<String>();
        for(Column column : columns){
            namesList.add(column.getName());
        }
        return namesList;
    }

    public int getRowCount(){
        return this.columns.getFirst().getSize();
    }

    private Column getColumn(String name){
        for (Column column : columns){
            if (column.getName().equals(name)){
                return column;
            }
        }
        return null;
    }

    public String getValue(String columnName, int row){
        Column col = getColumn(columnName);

        assert col != null;
        return col.getRowValue(row);
    }

    public void putValue(String columnName, int row, String value){
        Column col = getColumn(columnName);
        assert col != null;
        col.setRowValue(row, value);
    }

    public void addValue(String columnName, String value){
        Column col = getColumn(columnName);
        assert col != null;
        col.addRowValue(value);
    }

    public void removeRow(int index){
        for(Column column : columns){
            column.removeRow(index);
        }
    }
}
