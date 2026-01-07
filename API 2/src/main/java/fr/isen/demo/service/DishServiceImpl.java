package fr.isen.demo.service;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.Order;

public class DishServiceImpl implements DishService {

    // --- CONSTANTES DE CONNEXION ---
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // Constructeur pour charger le driver une seule fois
    public DishServiceImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Dish> getAllDishes() {
//begin of modifiable zone................T/da1f72bf-8c0a-4f1d-a080-1bd60e1f3176
        List<Dish> dishes = new ArrayList<>();
        // On récupère simplement la table Dish (pas besoin de JOIN complexe)
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

                // --- CORRECTION CRITIQUE ---
                // On récupère l'entier (int) pour que ça colle avec Modelio
                d.setCategory(rs.getInt("category_id"));

                d.setImagePath(rs.getString("image_path"));
                dishes.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/da1f72bf-8c0a-4f1d-a080-1bd60e1f3176
        return dishes;
    }

    @Override
    public void addDish(final Dish dish) {
//begin of modifiable zone................T/10c87e76-4261-4543-a17d-d89b38d22ee2
        String query = "INSERT INTO Dish (id, name, description, price, category_id, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dish.getId());
            ps.setString(2, dish.getName());
            ps.setString(3, dish.getDescription());
            ps.setDouble(4, dish.getPrice());
            // On insère l'entier directement
            ps.setInt(5, dish.getCategory());
            ps.setString(6, dish.getImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/10c87e76-4261-4543-a17d-d89b38d22ee2
    }

    @Override
    public void deleteDish(final String id) {
//begin of modifiable zone................T/21f5113a-1717-4b0f-afce-9db91e5f6a1c
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Dish WHERE id = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//end of modifiable zone..................E/21f5113a-1717-4b0f-afce-9db91e5f6a1c
    }

    @Override
    public Order createOrder(final int table, final List<String> ids) {
//begin of modifiable zone................T/f4aaa72b-a2aa-46be-9915-dd9271b2199c
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
//end of modifiable zone..................E/f4aaa72b-a2aa-46be-9915-dd9271b2199c
        return order;
    }

    public double getTotalSales() {
//begin of modifiable zone................T/321260fc-e446-48bc-9a86-81f1ec1065bf
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
//end of modifiable zone..................E/321260fc-e446-48bc-9a86-81f1ec1065bf
        return total;
    }
}