package fr.isen.demo;

import io.javalin.Javalin;
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.Order;
import fr.isen.demo.model.OrderRequest; // Import important !
import fr.isen.demo.service.DishService;
import fr.isen.demo.service.DishServiceImpl;

public class Main {
    private static DishService dishService = new DishServiceImpl();

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(7001);

        System.out.println("🚀 Serveur Restaurant démarré sur http://localhost:7001 (Version Modelio)");

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

        app.delete("/menu/{id}", ctx -> {
            String id = ctx.pathParam("id");
            dishService.deleteDish(id);
            ctx.status(204);
        });

        // --- SECTION : GESTION DES COMMANDES (CORRIGÉE) ---
        // C'est ici que j'ai tout changé pour utiliser OrderRequest
        app.post("/orders", ctx -> {
            try {
                // 1. On transforme le JSON reçu directement en objet OrderRequest
                // C'est magique : ça remplit dishIds (List<String>) tout seul !
                OrderRequest request = ctx.bodyAsClass(OrderRequest.class);

                // 2. Appel du service (Maintenant les types sont bons !)
                Order order = dishService.createOrder(
                        request.getTableNumber(),
                        request.getDishIds() // C'est bien une List<String> !
                );

                ctx.status(201).json(order);

            } catch (Exception e) {
                System.err.println("❌ Erreur de traitement commande : " + e.getMessage());
                // e.printStackTrace(); // Décommente si tu veux voir le détail
                ctx.status(400).result("Format de commande invalide : " + e.getMessage());
            }
        });

        // --- SECTION : STATUS ---

        app.get("/", ctx -> ctx.result("API Restaurant Modelio opérationnelle."));

        app.get("/status", ctx -> {
            ctx.json(new StatusResponse("Opérationnel", "Base Modelio connectée"));
        });

        app.get("/sales/total", ctx -> {
            double total = dishService.getTotalSales();
            ctx.json(new SalesResponse(total));
        });
    }

    // Classes internes pour les réponses JSON simples
    static class StatusResponse {
        public String status;
        public String message;
        public StatusResponse(String s, String m) { this.status = s; this.message = m; }
    }
}

class SalesResponse {
    public double totalSales;
    public SalesResponse(double totalSales) { this.totalSales = totalSales; }
}