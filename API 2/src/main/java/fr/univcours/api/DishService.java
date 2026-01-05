package fr.univcours.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishService {
    // Note le changement du nom de la base ici
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM dishes")) {

            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setPrice(rs.getDouble("price"));
                dish.setCategory(rs.getString("category"));
                dishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du menu");
        }
        return dishes;
    }
}