package fr.univcours.api;

import java.sql.*;
import java.util.ArrayList;
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
                d.setCategory(rs.getInt("category_id"));
                d.setImagePath(rs.getString("image_path"));
                dishes.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return dishes;
    }

    public void addDish(Dish dish) {
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

    public Order createOrder(int tableNumber, List<Map<String, Object>> itemsData) {
        if (itemsData == null || itemsData.isEmpty()) throw new IllegalArgumentException("Panier vide");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            conn.setAutoCommit(false);
            try {
                // 1. Calcul du total
                double total = 0;
                for (Map<String, Object> item : itemsData) {
                    String dishId = (String) item.get("dishId");

                    // Utilisation de Number pour éviter ClassCastException
                    Number quantityNum = (Number) item.get("quantity");
                    int quantity = quantityNum.intValue();

                    String q = "SELECT price FROM Dish WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(q)) {
                        ps.setString(1, dishId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                total += (rs.getDouble("price") * quantity);
                            }
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

                // 3. Insertion Items avec Piment et Accompagnement (Utilisation de Number)
                String insertItem = "INSERT INTO order_items (order_id, dish_id, quantity, piment, accompagnement) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement psItem = conn.prepareStatement(insertItem)) {
                    for (Map<String, Object> item : itemsData) {
                        psItem.setInt(1, generatedOrderId);
                        psItem.setString(2, (String) item.get("dishId"));

                        // Conversion sécurisée des nombres
                        psItem.setInt(3, ((Number) item.get("quantity")).intValue());
                        psItem.setInt(4, ((Number) item.get("piment")).intValue());
                        psItem.setInt(5, ((Number) item.get("accompagnement")).intValue());

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