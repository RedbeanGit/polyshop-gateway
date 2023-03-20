package fr.dopolytech.polyshop.gateway.models;

public class InventoryProduct {
    public String id;
    public double price;
    public int quantity;

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
