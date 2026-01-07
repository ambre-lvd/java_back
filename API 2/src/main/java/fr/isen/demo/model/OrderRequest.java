package fr.isen.demo.model;

import java.util.ArrayList;
import java.util.List;

public class OrderRequest {
    private int tableNumber;

    public int getTableNumber() {
        // Automatically generated method. Please do not modify this code.
        return this.tableNumber;
    }

    public void setTableNumber(final int value) {
        // Automatically generated method. Please do not modify this code.
        this.tableNumber = value;
    }

    private List<String> dishIds = new ArrayList<String> ();

    public List<String> getDishIds() {
        // Automatically generated method. Please do not modify this code.
        return this.dishIds;
    }

    public void setDishIds(final List<String> value) {
        // Automatically generated method. Please do not modify this code.
        this.dishIds = value;
    }

}
