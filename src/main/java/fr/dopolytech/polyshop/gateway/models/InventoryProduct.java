package fr.dopolytech.polyshop.gateway.models;

public class InventoryProduct {
    public String id;
    public String productId;
    public Integer quantity;

    public InventoryProduct() {
    }

    public InventoryProduct(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
