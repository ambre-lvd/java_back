package fr.univcours.api;

import io.javalin.Javalin;
// NOUVEAUX IMPORTS MODELIO
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.Order;
import fr.isen.demo.service.DishService;
import fr.isen.demo.service.DishServiceImpl;

// Garde ces imports utilitaires si besoin
import java.util.List;
import java.util.Map;
import java.util.List;

/**
 * Classe principale pour le projet RestaurantConsole.
 * Gère les routes API pour le menu et les commandes.
 */
public class Main {
    private static DishService dishService = new DishServiceImpl();

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(7001);

        System.out.println("🚀 Serveur Restaurant démarré sur http://localhost:7001");

        // --- SECTION : GESTION DE LA CARTE (MENU) ---

        app.get("/menu", ctx -> {
            ctx.json(dishService.getAllDishes());
        });

        app.post("/menu", ctx -> {
            try {
                Dish newDish = ctx.bodyAsClass(Dish.class);
                dishService.addDish(newDish);
                ctx.status(201).json(newDish);
            } catch (Exception e) {
                ctx.status(400).result("Erreur lors de l'ajout : " + e.getMessage());
            }
        });

        // CORRECTION : On ne fait plus de Integer.parseInt car l'ID est un String (ex: "E1")
        app.delete("/menu/{id}", ctx -> {
            String id = ctx.pathParam("id");
            dishService.deleteDish(id);
            ctx.status(204);
        });

        // --- SECTION : GESTION DES COMMANDES (ADDITION) ---

        app.post("/orders", ctx -> {
            try {
                Map<String, Object> body = ctx.bodyAsClass(Map.class);

                // Utilisation de Number pour éviter le ClassCastException
                Number tn = (Number) body.get("tableNumber");
                int tableNumber = tn.intValue(); // Convertit proprement en int

                List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");

                Order order = dishService.createOrder(tableNumber, items);
                ctx.status(201).json(order);

            } catch (Exception e) {
                System.err.println("❌ Erreur de traitement JSON : " + e.getMessage());
                ctx.status(400).result("Format de commande invalide : " + e.getMessage());
            }
        });

        // --- SECTION : STATUT ET STATISTIQUES ---

        app.get("/", ctx -> ctx.result("API RestaurantConsole opérationnelle."));

        app.get("/status", ctx -> {
            ctx.json(new StatusResponse("Opérationnel", "Base de données connectée"));
        });

        app.get("/sales/total", ctx -> {
            double total = dishService.getTotalSales();
            ctx.json(new SalesResponse(total));
        });
    }

    // Classes internes pour les réponses JSON
    static class StatusResponse {
        public String status;
        public String message;
        public StatusResponse(String s, String m) { this.status = s; this.message = m; }
    }
}

// Classe pour la réponse des ventes
class SalesResponse {
    public double totalSales;
    public SalesResponse(double totalSales) { this.totalSales = totalSales; }
}

