package fr.univcours.api;

import java.util.List;

public class Order {
    private int id;
    private int tableNumber;
    private double totalAmount;
    private List<Dish> items; // Liste des objets Dish (contenant l'ID en String)

    public Order() {}

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Dish> getItems() {
        return items;
    }

    public void setItems(List<Dish> items) {
        this.items = items;
    }
}