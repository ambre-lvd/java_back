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

    private List<DishItemRequest> items = new ArrayList<DishItemRequest> ();

    public List<DishItemRequest> getItems() {
        // Automatically generated method. Please do not modify this code.
        return this.items;
    }

    public void setItems(final List<DishItemRequest> value) {
        // Automatically generated method. Please do not modify this code.
        this.items = value;
    }

    public DishItemRequest dishItemRequest;

}
