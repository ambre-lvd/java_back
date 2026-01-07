package fr.isen.demo.service;

import java.util.List;
import java.util.ArrayList;
import java.sql.*; // Import indispensable pour SQL (Connection, ResultSet, etc.)
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.Order;

public class DishServiceImpl implements DishService {

    // --- 1. CONSTANTES DE CONNEXION (Elles manquaient) ---
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // Constructeur pour charger le driver
    public DishServiceImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Dish> getAllDishes() {
//begin of modifiable zone................
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT * FROM Dish";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getString("id"));
                d.setName(rs.getString("name"));
                d.setDescription(rs.getString("description"));
                d.setPrice(rs.getDouble("price"));
                // On récupère l'entier (int) pour que ça colle avec Modelio
                d.setCategory(rs.getInt("category_id"));
                d.setImagePath(rs.getString("image_path"));
                dishes.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................
        return dishes;
    }

    // --- CORRECTION DU PARAMÈTRE (dish avec minuscule) ---
    @Override
    public void addDish(final Dish dish) {
//begin of modifiable zone................
        String query = "INSERT INTO Dish (id, name, description, price, category_id, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dish.getId());
            ps.setString(2, dish.getName());
            ps.setString(3, dish.getDescription());
            ps.setDouble(4, dish.getPrice());
            ps.setInt(5, dish.getCategory());
            ps.setString(6, dish.getImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................
    }

    @Override
    public void deleteDish(final String id) {
//begin of modifiable zone................
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Dish WHERE id = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................
    }

    @Override
    public Order createOrder(final int table, final List<String> ids) {
//begin of modifiable zone................
        Order order = new Order();
        if (ids == null || ids.isEmpty()) return null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            // 1. Calcul du total
            double total = 0;
            for (String id : ids) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT price FROM Dish WHERE id = ?")) {
                    ps.setString(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) total += rs.getDouble("price");
                }
            }
            // 2. Création de la commande
            String sql = "INSERT INTO orders (table_number, total_amount) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, table);
                ps.setDouble(2, total);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) order.setId(rs.getInt(1));
            }
            order.setTotalAmount(total);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................
        return order;
    }

    public double getTotalSales() {
//begin of modifiable zone................
        double total = 0;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount) FROM orders")) {
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................
        return total;
    }
}