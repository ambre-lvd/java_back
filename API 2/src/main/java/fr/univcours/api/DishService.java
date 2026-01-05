package fr.univcours.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishService {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public DishService() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // --- MÉTHODES POUR LE MENU (ADMIN & BORNE) ---

    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String query = "SELECT * FROM dishes";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setPrice(rs.getDouble("price"));
                d.setCategory(rs.getString("category"));
                dishes.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return dishes;
    }

    public void addDish(Dish dish) {
        String query = "INSERT INTO dishes (name, price, category) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dish.getName());
            ps.setDouble(2, dish.getPrice());
            ps.setString(3, dish.getCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion plat: " + e.getMessage());
        }
    }

    public void deleteDish(int id) {
        String query = "DELETE FROM dishes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression plat: " + e.getMessage());
        }
    }

    // --- MÉTHODES POUR LES COMMANDES (BORNE) ---

    public Order createOrder(int tableNumber, List<Integer> dishIds) {
        if (dishIds == null || dishIds.isEmpty()) throw new IllegalArgumentException("Panier vide");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            conn.setAutoCommit(false);
            try {
                // 1. Calcul du total
                double total = 0;
                for (int id : dishIds) {
                    String q = "SELECT price FROM dishes WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(q)) {
                        ps.setInt(1, id);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) total += rs.getDouble("price");
                        }
                    }
                }

                // 2. Insertion Commande
                int generatedOrderId = 0;
                String insertOrder = "INSERT INTO orders (table_number, total_amount) VALUES (?, ?)";
                try (PreparedStatement psOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                    psOrder.setInt(1, tableNumber);
                    psOrder.setDouble(2, total);
                    psOrder.executeUpdate();
                    try (ResultSet gk = psOrder.getGeneratedKeys()) {
                        if (gk.next()) generatedOrderId = gk.getInt(1);
                    }
                }

                // 3. Insertion Items avec Quantités
                String insertItem = "INSERT INTO order_items (order_id, dish_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement psItem = conn.prepareStatement(insertItem)) {
                    Map<Integer, Integer> counts = new HashMap<>();
                    for (int id : dishIds) {
                        counts.put(id, counts.getOrDefault(id, 0) + 1);
                    }
                    for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
                        psItem.setInt(1, generatedOrderId);
                        psItem.setInt(2, entry.getKey());
                        psItem.setInt(3, entry.getValue());
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }

                conn.commit();
                Order order = new Order();
                order.setId(generatedOrderId);
                order.setTotalAmount(total);
                return order;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public double getTotalSales() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount) FROM orders")) {
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (SQLException e) { return 0; }
    }
}