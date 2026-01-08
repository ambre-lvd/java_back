package fr.isen.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;

    public int getId() {
        // Automatically generated method. Please do not modify this code.
        return this.id;
    }

    public void setId(final int value) {
        // Automatically generated method. Please do not modify this code.
        this.id = value;
    }

    private double totalAmount;

    public double getTotalAmount() {
        // Automatically generated method. Please do not modify this code.
        return this.totalAmount;
    }

    public void setTotalAmount(final double value) {
        // Automatically generated method. Please do not modify this code.
        this.totalAmount = value;
    }

    private int tableNumber;

    public int getTableNumber() {
        // Automatically generated method. Please do not modify this code.
        return this.tableNumber;
    }

    public void setTableNumber(final int value) {
        // Automatically generated method. Please do not modify this code.
        this.tableNumber = value;
    }

    private Date orderDate;

    public Date getOrderDate() {
        // Automatically generated method. Please do not modify this code.
        return this.orderDate;
    }

    public void setOrderDate(final Date value) {
        // Automatically generated method. Please do not modify this code.
        this.orderDate = value;
    }

    private List<OrderItems> orderItems = new ArrayList<OrderItems> ();

    public List<OrderItems> getOrderItems() {
        // Automatically generated method. Please do not modify this code.
        return this.orderItems;
    }

    public void setOrderItems(final List<OrderItems> value) {
        // Automatically generated method. Please do not modify this code.
        this.orderItems = value;
    }

}
