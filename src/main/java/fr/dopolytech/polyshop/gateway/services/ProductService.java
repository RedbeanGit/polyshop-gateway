package fr.dopolytech.polyshop.gateway.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.dopolytech.polyshop.gateway.configurations.UriConfiguration;
import fr.dopolytech.polyshop.gateway.models.CatalogProduct;
import fr.dopolytech.polyshop.gateway.models.Product;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private final UriConfiguration uriConfiguration;
    private final WebClient.Builder webClientBuilder;

    public ProductService(UriConfiguration uriConfiguration, WebClient.Builder webClientBuilder) {
        this.uriConfiguration = uriConfiguration;
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Product> getProductFromCatalog(String productId, Integer quantity) {
        String catalogUrl = uriConfiguration.getCatalogUri();

        WebClient webClient = webClientBuilder.build();

        return webClient.get().uri(catalogUrl + "/products/" + productId).retrieve()
                .bodyToMono(CatalogProduct.class)
                .defaultIfEmpty(new CatalogProduct(productId, null, null, null))
                .map(catalogProduct -> new Product(catalogProduct.productId, catalogProduct.name,
                        catalogProduct.description, catalogProduct.price, quantity));
    }
}
