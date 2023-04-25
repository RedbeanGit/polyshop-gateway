package fr.dopolytech.polyshop.gateway.models;

public class Order {
    public String id;
    public String status;
    public String date;

    public Order(String id, String status, String date) {
        this.id = id;
        this.status = status;
        this.date = date;
    }
}
