package fr.dopolytech.polyshop.gateway.models;

public class Product {
    public String id;
    public String name;
    public String description;
    public double price;
    public int quantity;

    public Product(String id, String name, String description, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

}
