package fr.univcours.api;

import io.javalin.Javalin;
import java.util.List;

/**
 * Classe principale pour le projet RestaurantConsole.
 * Gère les routes API pour le menu et les commandes.
 */
public class Main {
    private static DishService dishService = new DishService();

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
                OrderRequest request = ctx.bodyAsClass(OrderRequest.class);
                // Le service attend maintenant List<String> dishIds
                Order finalOrder = dishService.createOrder(request.tableNumber, request.dishIds);
                ctx.status(201).json(finalOrder);
            } catch (Exception e) {
                ctx.status(400).result("Erreur commande : " + e.getMessage());
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

