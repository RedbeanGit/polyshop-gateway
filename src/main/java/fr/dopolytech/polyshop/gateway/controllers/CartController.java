package fr.dopolytech.polyshop.gateway.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import fr.dopolytech.polyshop.gateway.configurations.UriConfiguration;
import fr.dopolytech.polyshop.gateway.dtos.AddToCartDto;
import fr.dopolytech.polyshop.gateway.models.CartProduct;
import fr.dopolytech.polyshop.gateway.models.Product;
import fr.dopolytech.polyshop.gateway.services.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final UriConfiguration uriConfiguration;
    private final WebClient.Builder webClientBuilder;
    private final ProductService productService;

    public CartController(UriConfiguration uriConfiguration, WebClient.Builder webClientBuilder,
            ProductService productService) {
        this.uriConfiguration = uriConfiguration;
        this.webClientBuilder = webClientBuilder;
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> findAll() {
        String cartUrl = uriConfiguration.getCartUri();

        WebClient webClient = webClientBuilder.build();

        return webClient.get().uri(cartUrl + "/cart").retrieve().bodyToMono(CartProduct[].class)
                .flatMap(cartProducts -> {
                    List<Mono<Product>> monoProducts = new ArrayList<Mono<Product>>();
                    if (cartProducts == null) {
                        return Mono.just(new ArrayList<Product>());
                    }

                    for (CartProduct cartProduct : cartProducts) {
                        monoProducts.add(
                                this.productService.getProductFromCatalog(cartProduct.productId, cartProduct.quantity));
                    }
                    return Flux.merge(monoProducts).collectList();
                }).flatMapMany(Flux::fromIterable);
    }

    @PostMapping("/add")
    public Mono<Product> addToCart(@RequestBody AddToCartDto addToCartDto) {
        String cartUrl = uriConfiguration.getCartUri();

        WebClient webClient = webClientBuilder.build();

        return webClient.post().uri(cartUrl + "/cart/add").bodyValue(addToCartDto).retrieve()
                .bodyToMono(CartProduct.class)
                .flatMap(cartProduct -> this.productService.getProductFromCatalog(cartProduct.productId,
                        cartProduct.quantity));
    }

    @PostMapping("/remove")
    public Mono<Product> removeFromCart(@RequestBody AddToCartDto addToCartDto) {
        String cartUrl = uriConfiguration.getCartUri();

        WebClient webClient = webClientBuilder.build();

        return webClient.post().uri(cartUrl + "/cart/remove").bodyValue(addToCartDto).retrieve()
                .bodyToMono(CartProduct.class)
                .flatMap(cartProduct -> this.productService.getProductFromCatalog(cartProduct.productId,
                        cartProduct.quantity));
    }
}
