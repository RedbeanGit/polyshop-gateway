package fr.dopolytech.polyshop.gateway.models;

public class CatalogProduct {
    public String id;
    public String productId;
    public String name;
    public String description;
    public Double price;

    public CatalogProduct(String productId, String name, String description, Double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
