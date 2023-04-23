package fr.dopolytech.polyshop.gateway.models;

public class Product {
    public String id;
    public String name;
    public String description;
    public Double price;
    public Integer quantity;

    public Product(String id, String name, String description, Double price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

}
