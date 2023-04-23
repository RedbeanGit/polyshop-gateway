package fr.dopolytech.polyshop.gateway.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import fr.dopolytech.polyshop.gateway.configurations.UriConfiguration;
import fr.dopolytech.polyshop.gateway.models.CatalogProduct;
import fr.dopolytech.polyshop.gateway.models.InventoryProduct;
import fr.dopolytech.polyshop.gateway.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final UriConfiguration uriConfiguration;
    private final WebClient.Builder webClientBuilder;

    public ProductController(UriConfiguration uriConfiguration, WebClient.Builder webClientBuilder) {
        this.uriConfiguration = uriConfiguration;
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping
    public Flux<Product> findAll() {
        String catalogUrl = uriConfiguration.getCatalogUri();
        String inventoryUrl = uriConfiguration.getInventoryUri();

        WebClient webClient = webClientBuilder.build();

        return webClient.get().uri(inventoryUrl + "/products").retrieve()
                .bodyToMono(InventoryProduct[].class)
                .flatMap(inventoryProducts -> {
                    List<Mono<Product>> monoProducts = new ArrayList<Mono<Product>>();
                    if (inventoryProducts == null) {
                        return Mono.just(new ArrayList<Product>());
                    }

                    for (InventoryProduct inventoryProduct : inventoryProducts) {
                        monoProducts.add(webClient.get()
                                .uri(catalogUrl + "/products/" + inventoryProduct.productId).retrieve()
                                .bodyToMono(CatalogProduct.class)
                                .defaultIfEmpty(new CatalogProduct(inventoryProduct.productId, null, null, null))
                                .map(catalogProduct -> new Product(
                                        catalogProduct.productId,
                                        catalogProduct.name,
                                        catalogProduct.description,
                                        catalogProduct.price,
                                        inventoryProduct.quantity)));
                    }
                    return Flux.merge(monoProducts).collectList();
                }).flatMapMany(Flux::fromIterable);

    }

    @GetMapping("/{id}")
    public Mono<Product> findOne(String id) {
        String catalogUrl = uriConfiguration.getCatalogUri();
        String inventoryUrl = uriConfiguration.getInventoryUri();

        WebClient webClient = webClientBuilder.build();

        return webClient.get()
                .uri(inventoryUrl + "/products/" + id)
                .retrieve()
                .toEntity(InventoryProduct.class)
                .flatMap(inventoryResponse -> {
                    InventoryProduct inventoryProduct = inventoryResponse.getBody();

                    if (inventoryProduct == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
                    }

                    return webClient.get()
                            .uri(catalogUrl + "/products/" + id)
                            .retrieve()
                            .toEntity(CatalogProduct.class)
                            .map(catalogResponse -> {
                                CatalogProduct catalogProduct;
                                if (catalogResponse.getStatusCode().is2xxSuccessful()) {
                                    catalogProduct = catalogResponse.getBody();
                                } else {
                                    catalogProduct = new CatalogProduct(id, null, null, 0.0);
                                }

                                if (catalogProduct == null) {
                                    return new Product(id, null, null, 0.0, 0);
                                }

                                return new Product(
                                        inventoryProduct.productId,
                                        catalogProduct.name,
                                        catalogProduct.description,
                                        catalogProduct.price,
                                        inventoryProduct.quantity);
                            });
                });
    }
}
