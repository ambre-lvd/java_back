package fr.univcours.api;

import io.javalin.Javalin;
import java.util.List;

/**
 * Classe principale pour le projet RestaurantConsole.
 * Gère les routes API pour le menu et les commandes.
 */
public class Main {
    // Initialisation du service qui communique avec MySQL (restaurant_db)
    private static DishService dishService = new DishService();

    public static void main(String[] args) {

        // Création de l'instance Javalin sur le port 7001
        Javalin app = Javalin.create(config -> {
            // Activation du CORS pour permettre au Front-end de communiquer avec le Back-end
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(7001);

        System.out.println("🚀 Serveur Restaurant démarré sur http://localhost:7001");

        // --- SECTION : GESTION DE LA CARTE (MENU) ---

        // Route GET /menu : Récupère la liste de tous les plats
        app.get("/menu", ctx -> {
            List<Dish> menu = dishService.getMenu();
            ctx.json(menu);
        });

        // Route POST /menu : Ajoute un nouveau plat à la carte
        app.post("/menu", ctx -> {
            Dish newDish = ctx.bodyAsClass(Dish.class);
            dishService.addDish(newDish);
            ctx.status(201).json(newDish);
        });

        // Route DELETE /menu/{id} : Supprime un plat de la carte via son ID
        app.delete("/menu/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dishService.deleteDish(id);
            ctx.status(204).result("Plat supprimé");
        });


        // --- SECTION : GESTION DES COMMANDES (ADDITION) ---

        /**
         * Route POST /orders : Calcule l'addition pour une table.
         * Prend un JSON contenant le numéro de table et une liste d'IDs de plats.
         * Exemple JSON : { "tableNumber": 5, "dishIds": [1, 2, 4] }
         */
        app.post("/orders", ctx -> {
            // Utilise la classe utilitaire OrderRequest pour lire le JSON
            OrderRequest request = ctx.bodyAsClass(OrderRequest.class);

            // Appelle le service pour calculer le total et enregistrer en base
            Order finalOrder = dishService.createOrder(request.tableNumber, request.dishIds);

            // Renvoie l'objet Order complet (avec total et détails des plats)
            ctx.status(201).json(finalOrder);
        });


        // --- SECTION : STATUT DU SYSTÈME ---

        app.get("/", ctx -> {
            ctx.result("Bienvenue sur l'API RestaurantConsole. Utilisez /menu pour voir la carte.");
        });

        app.get("/status", ctx -> {
            ctx.json(new StatusResponse("Opérationnel", "Base de données restaurant_db connectée"));
        });

        app.get("/sales/total", ctx -> {
            double total = dishService.getTotalSales();
            ctx.json(new SalesResponse(total));
        });
        app.post("/menu", ctx -> {
            try {
                Dish newDish = ctx.bodyAsClass(Dish.class);
                dishService.addDish(newDish);
                ctx.status(201).json(newDish);
            } catch (IllegalArgumentException e) {
                ctx.status(400).result(e.getMessage()); // Renvoie une erreur 400 (Bad Request)
            }
        });

    }

    // Petite classe interne pour la réponse du statut (Optionnel)
    static class StatusResponse {
        public String status;
        public String message;
        public StatusResponse(String s, String m) { this.status = s; this.message = m; }
    }

}

class SalesResponse {
    public double totalSales;

    public SalesResponse(double totalSales) {
        this.totalSales = totalSales;
    }
}