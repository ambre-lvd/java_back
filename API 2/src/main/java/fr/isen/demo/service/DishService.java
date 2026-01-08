package fr.isen.demo.service;

import java.util.List;
import fr.isen.demo.model.Category;
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.DishItemRequest;
import fr.isen.demo.model.Order;

public interface DishService {
    List<Dish> getAllDishes();

    void addDish(final Dish dish);

    void deleteDish(final String id);

    Order createOrder(final int table, final List<DishItemRequest> items);

    double getTotalSales();

    List<Category> getAllCategories();

}
