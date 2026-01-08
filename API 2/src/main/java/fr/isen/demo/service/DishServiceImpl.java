package fr.isen.demo.service;

import java.util.List;
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.DishItemRequest;
import fr.isen.demo.model.Order;

public class DishServiceImpl implements DishService {
    public static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";

    public static final String USER = "root";

    public static final String PASS = "";

    public List<Dish> getAllDishes() {
//begin of modifiable zone................T/da1f72bf-8c0a-4f1d-a080-1bd60e1f3176
        // CODE BLINDÉ CONTRE MODELIO
        java.util.List<Dish> dishes = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Dish";
        String url = "jdbc:mysql://localhost:3306/restaurant_db";
        String user = "root";
        String pass = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, pass);
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(sql)) {
        
                while (rs.next()) {
                    Dish d = new Dish();
                    d.setId(rs.getString("id"));
                    d.setName(rs.getString("name"));
                    d.setDescription(rs.getString("description"));
                    d.setPrice(rs.getDouble("price"));
                    d.setCategory(rs.getInt("category_id"));
                    d.setImagePath(rs.getString("image_path"));
                    dishes.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/da1f72bf-8c0a-4f1d-a080-1bd60e1f3176
//begin of modifiable zone................T/9b814c07-28f2-49fd-b132-3277ba4e4af5
        return dishes;
//end of modifiable zone..................E/9b814c07-28f2-49fd-b132-3277ba4e4af5
    }

    public void addDish(final Dish dish) {
//begin of modifiable zone................T/10c87e76-4261-4543-a17d-d89b38d22ee2
        String query = "INSERT INTO Dish (id, name, description, price, category_id, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        String url = "jdbc:mysql://localhost:3306/restaurant_db";
        String user = "root";
        String pass = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, pass);
                 java.sql.PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, dish.getId());
                ps.setString(2, dish.getName());
                ps.setString(3, dish.getDescription());
                ps.setDouble(4, dish.getPrice());
                ps.setInt(5, dish.getCategory());
                ps.setString(6, dish.getImagePath());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/10c87e76-4261-4543-a17d-d89b38d22ee2
    }

    public void deleteDish(final String id) {
//begin of modifiable zone................T/21f5113a-1717-4b0f-afce-9db91e5f6a1c
        String url = "jdbc:mysql://localhost:3306/restaurant_db";
        String user = "root";
        String pass = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, pass);
                 java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM Dish WHERE id = ?")) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/21f5113a-1717-4b0f-afce-9db91e5f6a1c
    }

    public Order createOrder(final int table, final List<DishItemRequest> items) {
//begin of modifiable zone................T/f4aaa72b-a2aa-46be-9915-dd9271b2199c
        Order order = new Order();
        if (items == null || items.isEmpty()) return null;
        
        String url = "jdbc:mysql://localhost:3306/restaurant_db";
        String user = "root";
        String pass = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, pass)) {
        
                // 1. Calcul du total
                double total = 0;
                for (DishItemRequest item : items) {
                    try (java.sql.PreparedStatement ps = conn.prepareStatement("SELECT price FROM Dish WHERE id = ?")) {
                        ps.setString(1, item.getDishId());
                        java.sql.ResultSet rs = ps.executeQuery();
                        if (rs.next()) total += rs.getDouble("price");
                    }
                }
        
                // 2. Création de la commande principale
                int orderId = -1;
                String sql = "INSERT INTO orders (table_number, total_amount) VALUES (?, ?)";
                try (java.sql.PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, table);
                    ps.setDouble(2, total);
                    ps.executeUpdate();
                    java.sql.ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                        order.setId(orderId);
                    }
                }
                order.setTotalAmount(total);
        
                // 3. Insertion des détails (Table order_items) avec PIMENT et ACCOMPAGNEMENT
                if (orderId != -1) {
                    String sqlItems = "INSERT INTO order_items (order_id, dish_id, quantity, piment, accompagnement) VALUES (?, ?, 1, ?, ?)";
                    try (java.sql.PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                        for (DishItemRequest item : items) {
                            psItems.setInt(1, orderId);
                            psItems.setString(2, item.getDishId());
                            psItems.setInt(3, item.getPiment());
                            psItems.setInt(4, item.getAccompagnement());
                            psItems.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/f4aaa72b-a2aa-46be-9915-dd9271b2199c
//begin of modifiable zone................T/14442dc0-ac90-4dfd-9f00-075ae5b16cb2
        return order;
//end of modifiable zone..................E/14442dc0-ac90-4dfd-9f00-075ae5b16cb2
    }

    public double getTotalSales() {
//begin of modifiable zone................T/321260fc-e446-48bc-9a86-81f1ec1065bf
        double total = 0;
        String url = "jdbc:mysql://localhost:3306/restaurant_db";
        String user = "root";
        String pass = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, pass);
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount) FROM orders")) {
                if (rs.next()) {
                    total = rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/321260fc-e446-48bc-9a86-81f1ec1065bf
//begin of modifiable zone................T/e4525461-4e24-499d-9f29-c94f505c85e8
        return total;
//end of modifiable zone..................E/e4525461-4e24-499d-9f29-c94f505c85e8
    }

}
