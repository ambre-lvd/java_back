package fr.univcours.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les utilisateurs via une base de données MySQL
 */
public class UserService {
    // Configuration de la base de données
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/isen";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = ""; // Modifié de null à vide pour la compatibilité JDBC

    public UserService() {
        // On vérifie que le driver est présent (optionnel selon la version de Java)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL non trouvé !");
        }
    }

    /**
     * Récupère tous les utilisateurs depuis la base de données
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                // Si tu as un champ age dans ta table :
                // user.setAge(rs.getInt("age"));

                users.add(user);
            }
            System.out.println("✅ " + users.size() + " utilisateurs récupérés depuis la BD");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la lecture des utilisateurs : " + e.getMessage());
            throw new RuntimeException(e);
        }
        return users;
    }

    /**
     * Récupère un utilisateur par son ID
     */
    public Optional<User> getUserById(int id) {
        String query = "SELECT * FROM user WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche de l'utilisateur " + id);
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    /**
     * Ajoute un nouvel utilisateur dans la base de données
     */
    public User addUser(User user) {
        String query = "INSERT INTO user (name, email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.executeUpdate();

            // Récupère l'ID auto-généré par la base de données
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
            System.out.println("✅ Utilisateur ajouté avec l'ID : " + user.getId());
            return user;

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}