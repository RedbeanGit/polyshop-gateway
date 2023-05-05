package fr.dopolytech.polyshop.gateway.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UriConfiguration {
    @Value("${catalog.uri}")
    private String catalogUri;

    @Value("${cart.uri}")
    private String cartUri;

    @Value("${order.uri}")
    private String orderUri;

    @Value("${inventory.uri}")
    private String inventoryUri;

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
