package fr.dopolytech.polyshop.gateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import fr.dopolytech.polyshop.gateway.configurations.UriConfiguration;
import fr.dopolytech.polyshop.gateway.models.Order;
import fr.dopolytech.polyshop.gateway.models.OrderOrder;
import fr.dopolytech.polyshop.gateway.models.OrderProduct;
import fr.dopolytech.polyshop.gateway.models.Product;
import fr.dopolytech.polyshop.gateway.services.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final UriConfiguration uriConfiguration;
    private final WebClient.Builder webClientBuilder;
    private final ProductService productService;

    public OrderController(UriConfiguration uriConfiguration, WebClient.Builder webClientBuilder,
            ProductService productService) {
        this.uriConfiguration = uriConfiguration;
        this.webClientBuilder = webClientBuilder;
        this.productService = productService;
    }

    @GetMapping
    public Flux<Order> findAll() {
        String orderUri = uriConfiguration.getOrderUri();

        WebClient client = webClientBuilder.build();

        return client.get()
                .uri(orderUri + "/orders")
                .retrieve()
                .bodyToFlux(OrderOrder.class)
                .map(orderOrder -> new Order(orderOrder.orderId, orderOrder.status, orderOrder.date));
    }

    @GetMapping("/{id}")
    public Mono<Order> findById(@PathVariable String id) {
        String orderUri = uriConfiguration.getOrderUri();

        WebClient client = webClientBuilder.build();

        return client.get()
                .uri(orderUri + "/orders/" + id)
                .retrieve()
                .bodyToMono(OrderOrder.class)
                .map(orderOrder -> new Order(orderOrder.orderId, orderOrder.status, orderOrder.date));
    }

    @GetMapping("/{id}/products")
    public Flux<Product> findAllProducts(@PathVariable String id) {
        String orderUri = uriConfiguration.getOrderUri();

        WebClient client = webClientBuilder.build();

        return client.get()
                .uri(orderUri + "/orders/" + id + "/products")
                .retrieve()
                .bodyToFlux(OrderProduct.class)
                .flatMap(orderProduct -> productService.getProductFromCatalog(orderProduct.productId,
                        orderProduct.quantity));
    }
}
