package fr.dopolytech.polyshop.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UriConfiguration {
    private String catalogUri = "http://localhost:8081";
    private String cartUri = "http://localhost:8082";
    private String orderUri = "http://localhost:8083";
    private String inventoryUri = "http://localhost:8084";

    public String getCatalogUri() {
        return catalogUri;
    }

    public void setCatalogUri(String catalogUri) {
        this.catalogUri = catalogUri;
    }

    public String getCartUri() {
        return cartUri;
    }

    public void setCartUri(String cartUri) {
        this.cartUri = cartUri;
    }

    public String getOrderUri() {
        return orderUri;
    }

    public void setOrderUri(String orderUri) {
        this.orderUri = orderUri;
    }

    public String getInventoryUri() {
        return inventoryUri;
    }

    public void setInventoryUri(String inventoryUri) {
        this.inventoryUri = inventoryUri;
    }
}
