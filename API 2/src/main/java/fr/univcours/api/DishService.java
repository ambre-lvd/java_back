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

    public List<Dish> getMenu() {
        return getAllDishes();
    }

    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        // TABLE CORRIGÉE : Dish
        String query = "SELECT id, name, description, price, category_id, image_path FROM Dish";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getString("id"));
                d.setName(rs.getString("name"));
                d.setDescription(rs.getString("description"));
                d.setPrice(rs.getDouble("price"));
                // COLONNE CORRIGÉE : category_id
                d.setCategory(rs.getInt("category_id"));
                d.setImagePath(rs.getString("image_path"));
                dishes.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return dishes;
    }

    public void addDish(Dish dish) {
        // TABLE CORRIGÉE : Dish | COLONNE CORRIGÉE : category_id
        String query = "INSERT INTO Dish (id, name, description, price, category_id, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dish.getId());
            ps.setString(2, dish.getName());
            ps.setString(3, dish.getDescription());
            ps.setDouble(4, dish.getPrice());
            ps.setInt(5, dish.getCategory());
            ps.setString(6, dish.getImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion plat: " + e.getMessage());
        }
    }

    public void deleteDish(String id) {
        // TABLE CORRIGÉE : Dish
        String query = "DELETE FROM Dish WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression plat: " + e.getMessage());
        }
    }

    // --- MÉTHODES POUR LES COMMANDES ---

    public Order createOrder(int tableNumber, List<String> dishIds) {
        if (dishIds == null || dishIds.isEmpty()) throw new IllegalArgumentException("Panier vide");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            conn.setAutoCommit(false);
            try {
                // 1. Calcul du total (Requête sur la table Dish)
                double total = 0;
                for (String id : dishIds) {
                    String q = "SELECT price FROM Dish WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(q)) {
                        ps.setString(1, id);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) total += rs.getDouble("price");
                        }
                    }
                }

                // 2. Insertion Commande (Table orders reste inchangée)
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

                // 3. Insertion Items (Lien vers la table Dish)
                String insertItem = "INSERT INTO order_items (order_id, dish_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement psItem = conn.prepareStatement(insertItem)) {
                    Map<String, Integer> counts = new HashMap<>();
                    for (String id : dishIds) {
                        counts.put(id, counts.getOrDefault(id, 0) + 1);
                    }
                    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                        psItem.setInt(1, generatedOrderId);
                        psItem.setString(2, entry.getKey());
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