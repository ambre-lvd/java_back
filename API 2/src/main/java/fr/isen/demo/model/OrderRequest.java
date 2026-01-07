package fr.isen.demo.model;

import java.util.List;
import java.util.ArrayList;

public class OrderRequest {
    private int tableNumber;
    private List<String> dishIds = new ArrayList<>();

    // Constructeur vide (obligatoire pour que le JSON fonctionne)
    public OrderRequest() {
    }

    public int getTableNumber() {
        return this.tableNumber;
    }

    public void setTableNumber(int value) {
        this.tableNumber = value;
    }

    public List<String> getDishIds() {
        return this.dishIds;
    }

    public void setDishIds(List<String> value) {
        this.dishIds = value;
    }
}