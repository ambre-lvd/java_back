package Impl;

import java.util.ArrayList;
import java.util.List;
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.Category;
import fr.isen.demo.model.DishItemRequest;
import fr.isen.demo.model.Order;
import fr.isen.demo.service.DishService;

public class DishServiceImpl implements DishService {

    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASS = "";

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASS);
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Category c = new Category();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    categories.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT * FROM Dish";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASS);
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Dish d = new Dish();
                    d.setId(rs.getString("id"));
                    d.setName(rs.getString("name"));
                    d.setDescription(rs.getString("description"));
                    d.setPrice(rs.getDouble("price"));
                    d.setImagePath(rs.getString("image_path"));

                    // --- ADAPTATION POUR L'ASSOCIATION ---
                    // On crée un objet Category juste pour stocker l'ID récupéré
                    Category cat = new Category();
                    cat.setId(rs.getInt("category_id"));
                    d.setCategory(cat);
                    // -------------------------------------

                    // Gestion sécurisée de la disponibilité
                    try {
                        d.setDisponibilite(rs.getBoolean("disponibilite"));
                    } catch (Exception e) {
                        d.setDisponibilite(true);
                    }

                    dishes.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishes;
    }

    @Override
    public void addDish(final Dish dish) {
        String query = "INSERT INTO Dish (id, name, description, price, category_id, image_path, disponibilite) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASS);
                 java.sql.PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, dish.getId());
                ps.setString(2, dish.getName());
                ps.setString(3, dish.getDescription());
                ps.setDouble(4, dish.getPrice());

                // --- ADAPTATION : On récupère l'ID depuis l'objet Category ---
                if (dish.getCategory() != null) {
                    ps.setInt(5, dish.getCategory().getId());
                } else {
                    ps.setNull(5, java.sql.Types.INTEGER);
                }
                // -------------------------------------------------------------

                ps.setString(6, dish.getImagePath());
                ps.setBoolean(7, dish.isDisponibilite());

                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDish(final String id) {
        String sql = "DELETE FROM Dish WHERE id = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASS);
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order createOrder(final int table, final List<DishItemRequest> items) {
        Order order = new Order();
        if (items == null || items.isEmpty()) return null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASS)) {

                // 1. Calcul du total
                double total = 0;
                for (DishItemRequest item : items) {
                    try (java.sql.PreparedStatement ps = conn.prepareStatement("SELECT price FROM Dish WHERE id = ?")) {
                        ps.setString(1, item.getDishId());
                        java.sql.ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            total += rs.getDouble("price");
                        }
                    }
                }

                // 2. Commande principale
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
                order.setTableNumber(table);

                // 3. Détails
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
        return order;
    }

    @Override
    public double getTotalSales() {
        double total = 0;
        String sql = "SELECT SUM(total_amount) FROM orders";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL, USER, PASS);
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    total = rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}