package fr.dopolytech.polyshop.gateway.models;

public class CatalogProduct {
    public String id;
    public String name;
    public String description;
    public String inventoryId;

    public CatalogProduct(String inventoryId, String name, String description) {
        this.inventoryId = inventoryId;
        this.name = name;
        this.description = description;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
