package fr.univcours.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les utilisateurs
 * Charge les utilisateurs depuis un fichier JSON
 */
public class UserService {
    private List<User> users;
    private int nextId;
    private final ObjectMapper objectMapper;

    public UserService() {
        this.objectMapper = new ObjectMapper();
        this.users = new ArrayList<>();
        this.nextId = 1;

        // Charger les utilisateurs depuis le fichier JSON
        loadUsersFromJson();
    }

    /**
     * Charge les utilisateurs depuis users.json
     */
    private void loadUsersFromJson() {
        try {
            // Charger le fichier depuis les ressources
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("users.json");

            if (inputStream == null) {
                System.err.println("⚠️  Fichier non trouvé, données par défaut");
                initializeDefaultUsers();
                return;
            }

            // Désérialiser le JSON en liste d'utilisateurs
            users = objectMapper.readValue(inputStream,
                    new TypeReference<List<User>>() {});

            // Calculer le prochain ID disponible
            nextId = users.stream()
                    .mapToInt(User::getId)
                    .max()
                    .orElse(0) + 1;

            System.out.println("✅ " + users.size() +
                    " utilisateurs chargés depuis users.json");

        } catch (IOException e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            initializeDefaultUsers();
        }
    }

    /**
     * Initialise des utilisateurs par défaut (fallback)
     */
    private void initializeDefaultUsers() {
        users.add(new User(nextId++, "Alice Dupont", "alice@example.com", 25));
        users.add(new User(nextId++, "Bob Martin", "bob@example.com", 30));
        System.out.println("✅ Utilisateurs par défaut initialisés");
    }

    /**
     * Récupère tous les utilisateurs
     */
    public List<User> getAllUsers() {
        return users;
    }

    /**
     * Récupère un utilisateur par son ID
     */
    public Optional<User> getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    /**
     * Ajoute un nouvel utilisateur
     */
    public User addUser(User user) {
        user.setId(nextId++);
        users.add(user);
        return user;
    }
}