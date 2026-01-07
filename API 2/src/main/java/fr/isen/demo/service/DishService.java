package fr.isen.demo.service;

import java.util.List;
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.Order;

public interface DishService {
    List<Dish> getAllDishes();

    void addDish(final Dish Dish);

    void deleteDish(final String id);

    Order createOrder(final int table, final List<String> ids);

    double getTotalSales();

}
