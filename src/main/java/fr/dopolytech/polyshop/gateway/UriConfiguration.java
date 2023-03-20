package fr.dopolytech.polyshop.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UriConfiguration {
    private String catalogUri = "lb://catalog-service";
    private String cartUri = "lb://cart-service";
    private String orderUri = "lb://order-service";
    private String inventoryUri = "lb://inventory-service";

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
