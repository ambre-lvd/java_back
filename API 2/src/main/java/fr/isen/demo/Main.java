package fr.isen.demo;

import io.javalin.Javalin;
import fr.isen.demo.model.Dish;
import fr.isen.demo.model.Order;
import fr.isen.demo.model.OrderRequest;
import fr.isen.demo.model.DishItemRequest;
import fr.isen.demo.service.DishService;
import fr.isen.demo.service.DishServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static DishService dishService = new DishServiceImpl();

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(7001);

        System.out.println("🚀 Serveur Restaurant démarré sur http://localhost:7001");

        // --- 1. MENU ---

        app.get("/menu", ctx -> {
            ctx.json(dishService.getAllDishes());
        });

        app.post("/menu", ctx -> {
            try {
                Dish newDish = ctx.bodyAsClass(Dish.class);
                dishService.addDish(newDish);
                ctx.status(201).json(newDish);
            } catch (Exception e) {
                ctx.status(400).result("Erreur ajout : " + e.getMessage());
            }
        });

        app.delete("/menu/{id}", ctx -> {
            String id = ctx.pathParam("id");
            dishService.deleteDish(id);
            ctx.status(204);
        });

        // --- 2. COMMANDES (VERSION FINALE) ---

        app.post("/orders", ctx -> {
            try {
                OrderRequest request = ctx.bodyAsClass(OrderRequest.class);

                // CORRECTION ICI : Modelio renvoie déjà une List, on l'utilise directement !
                List<DishItemRequest> itemsList = request.getItems();

                // Petite sécurité au cas où la liste serait null
                if (itemsList == null) {
                    itemsList = new ArrayList<>();
                }

                Order order = dishService.createOrder(
                        request.getTableNumber(),
                        itemsList
                );

                ctx.status(201).json(order);

            } catch (Exception e) {
                System.err.println("❌ Erreur commande : " + e.getMessage());
                ctx.status(400).result("Erreur : " + e.getMessage());
            }
        });

        // --- 3. STATUS ---

        app.get("/", ctx -> ctx.result("API Restaurant opérationnelle."));

        app.get("/status", ctx -> {
            ctx.json(new StatusResponse("Opérationnel", "Connecté BDD"));
        });

        app.get("/sales/total", ctx -> {
            ctx.json(new SalesResponse(dishService.getTotalSales()));
        });
    }

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