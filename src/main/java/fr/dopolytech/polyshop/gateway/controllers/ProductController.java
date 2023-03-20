package fr.dopolytech.polyshop.gateway.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.dopolytech.polyshop.gateway.UriConfiguration;
import fr.dopolytech.polyshop.gateway.models.CatalogProduct;
import fr.dopolytech.polyshop.gateway.models.InventoryProduct;
import fr.dopolytech.polyshop.gateway.models.Product;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final UriConfiguration uriConfiguration;

    public ProductController(UriConfiguration uriConfiguration) {
        this.uriConfiguration = uriConfiguration;
    }

    @GetMapping
    public List<Product> findAll() {
        String catalogUrl = uriConfiguration.getCatalogUri();
        String inventoryUrl = uriConfiguration.getInventoryUri();

        RestTemplate restTemplate = new RestTemplate();
        List<Product> products = new ArrayList<Product>();

        ResponseEntity<InventoryProduct[]> inventoryProductsResponse = restTemplate.getForEntity(
                inventoryUrl + "/products",
                InventoryProduct[].class);

        if (inventoryProductsResponse.getBody() == null) {
            return products;
        }

        List<InventoryProduct> inventoryProducts = List.of(inventoryProductsResponse.getBody());

        List<CatalogProduct> currentCatalogProducts;
        CatalogProduct currentCatalogProduct;

        for (InventoryProduct inventoryProduct : inventoryProducts) {
            ResponseEntity<CatalogProduct[]> catalogProductsResponse = restTemplate.getForEntity(
                    catalogUrl + "/products?inventory=" + inventoryProduct.getId(),
                    CatalogProduct[].class);
            if (catalogProductsResponse.getBody() != null) {
                currentCatalogProducts = List.of(catalogProductsResponse.getBody());
            } else {
                currentCatalogProducts = new ArrayList<CatalogProduct>();
                currentCatalogProducts.add(new CatalogProduct(inventoryProduct.getId(), "Unknown",
                        "Unknown"));
            }
            currentCatalogProduct = currentCatalogProducts.get(0);
            products.add(new Product(
                    currentCatalogProduct.getInventoryId(),
                    currentCatalogProduct.getName(),
                    currentCatalogProduct.getDescription(),
                    inventoryProduct.getPrice(),
                    inventoryProduct.getQuantity()));
        }

        return products;
    }

    @GetMapping("/{id}")
    public Product findOne(String id) {
        String catalogUrl = uriConfiguration.getCatalogUri();
        String inventoryUrl = uriConfiguration.getInventoryUri();

        RestTemplate restTemplate = new RestTemplate();
        List<CatalogProduct> catalogProducts;
        CatalogProduct catalogProduct;
        InventoryProduct inventoryProduct;

        ResponseEntity<InventoryProduct> inventoryProductResponse = restTemplate.getForEntity(
                inventoryUrl + "/products/" + id,
                InventoryProduct.class);
        ResponseEntity<CatalogProduct[]> catalogProductsResponse = restTemplate.getForEntity(
                catalogUrl + "/products?inventory=" + id,
                CatalogProduct[].class);

        catalogProducts = List.of(catalogProductsResponse.getBody());
        catalogProduct = catalogProducts.get(0);
        inventoryProduct = inventoryProductResponse.getBody();

        if (inventoryProduct == null) {
            return null;
        }

        if (catalogProduct == null) {
            return new Product(id, "Unknown", "Unknown", inventoryProduct.getPrice(),
                    inventoryProduct.getQuantity());
        }

        return new Product(catalogProduct.getInventoryId(), catalogProduct.getName(), catalogProduct.getDescription(),
                inventoryProduct.getPrice(), inventoryProduct.getQuantity());
    }
}
