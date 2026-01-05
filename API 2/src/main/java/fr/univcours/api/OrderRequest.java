package fr.univcours.api;

import java.util.List;

public class OrderRequest {
    public int tableNumber;
    public List<String> dishIds; // Assure-toi que c'est bien List<String> ici aussi !

    public OrderRequest() {} // Constructeur vide nécessaire pour Javalin/Jackson
}