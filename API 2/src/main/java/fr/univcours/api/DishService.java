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

    // --- MÉTHODES POUR LE MENU ---

    public List<Dish> getMenu() { // Alias pour ton Main
        return getAllDishes();
    }

    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String query = "SELECT * FROM dishes";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getString("id")); // CHANGÉ : getString au lieu de getInt
                d.setName(rs.getString("name"));
                d.setDescription(rs.getString("description"));
                d.setPrice(rs.getDouble("price"));
                d.setCategory(rs.getString("category"));
                d.setImagePath(rs.getString("image_path"));
                dishes.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return dishes;
    }

    public void addDish(Dish dish) {
        String query = "INSERT INTO dishes (id, name, description, price, category, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dish.getId()); // Ajout de l'ID manuel (E1, P1...)
            ps.setString(2, dish.getName());
            ps.setString(3, dish.getDescription());
            ps.setDouble(4, dish.getPrice());
            ps.setString(5, dish.getCategory());
            ps.setString(6, dish.getImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion plat: " + e.getMessage());
        }
    }

    public void deleteDish(String id) { // CHANGÉ : Argument String
        String query = "DELETE FROM dishes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id); // CHANGÉ : setString
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression plat: " + e.getMessage());
        }
    }

    // --- MÉTHODES POUR LES COMMANDES ---

    public Order createOrder(int tableNumber, List<String> dishIds) { // CHANGÉ : List<String>
        if (dishIds == null || dishIds.isEmpty()) throw new IllegalArgumentException("Panier vide");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            conn.setAutoCommit(false);
            try {
                // 1. Calcul du total
                double total = 0;
                for (String id : dishIds) { // CHANGÉ : String id
                    String q = "SELECT price FROM dishes WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(q)) {
                        ps.setString(1, id); // CHANGÉ : setString
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
                    Map<String, Integer> counts = new HashMap<>(); // CHANGÉ : Map<String, Integer>
                    for (String id : dishIds) {
                        counts.put(id, counts.getOrDefault(id, 0) + 1);
                    }
                    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                        psItem.setInt(1, generatedOrderId);
                        psItem.setString(2, entry.getKey()); // CHANGÉ : setString pour le dish_id
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