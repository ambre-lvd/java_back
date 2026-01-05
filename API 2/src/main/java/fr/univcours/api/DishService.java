package fr.univcours.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service gérant la logique métier du restaurant :
 * Menu (Dishes) et Commandes (Orders).
 */
public class DishService {
    // Configuration de la nouvelle base de données dédiée
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    /**
     * Récupère l'intégralité de la carte du restaurant.
     */
    public List<Dish> getMenu() {
        List<Dish> menu = new ArrayList<>();
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
                menu.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération du menu");
        }
        return menu;
    }

    /**
     * Ajoute un nouveau plat à la base de données.
     */
    public void addDish(Dish dish) {
        if (dish.getPrice() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        String query = "INSERT INTO dishes (name, price, category) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, dish.getName());
            pstmt.setDouble(2, dish.getPrice());
            pstmt.setString(3, dish.getCategory());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout du plat");
        }
    }

    /**
     * Supprime un plat de la carte par son ID.
     */
    public void deleteDish(int id) {
        String query = "DELETE FROM dishes WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du plat");
        }
    }

    /**
     * Crée une commande, calcule le total (l'addition) et l'enregistre en base.
     */
    public Order createOrder(int tableNumber, List<Integer> dishIds) {
        double total = 0;
        List<Dish> dishesOrdered = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            // 1. Calcul du total et récupération des détails des plats
            for (int id : dishIds) {
                String q = "SELECT * FROM dishes WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(q)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Dish d = new Dish();
                            d.setId(rs.getInt("id"));
                            d.setName(rs.getString("name"));
                            d.setPrice(rs.getDouble("price"));
                            total += d.getPrice();
                            dishesOrdered.add(d);
                        }
                    }
                }
            }

            // 2. Enregistrement de la commande dans la table 'orders'
            String insertOrder = "INSERT INTO orders (table_number, total_amount) VALUES (?, ?)";
            try (PreparedStatement psOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, tableNumber);
                psOrder.setDouble(2, total);
                psOrder.executeUpdate();

                // Récupération de l'ID généré pour la commande
                int generatedId = 0;
                try (ResultSet gk = psOrder.getGeneratedKeys()) {
                    if (gk.next()) {
                        generatedId = gk.getInt(1);
                    }
                }

                // 3. Création de l'objet de réponse
                Order order = new Order();
                order.setId(generatedId);
                order.setTableNumber(tableNumber);
                order.setTotalAmount(total);
                order.setItems(dishesOrdered);

                System.out.println("✅ Commande enregistrée - Table " + tableNumber + " - Total: " + total + "€");
                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la commande");
        }
    }

    public double getTotalSales() {
        double totalSales = 0;
        String query = "SELECT SUM(total_amount) as total FROM orders";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                totalSales = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSales;
    }
}