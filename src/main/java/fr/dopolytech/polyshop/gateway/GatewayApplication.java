package fr.dopolytech.polyshop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import fr.dopolytech.polyshop.gateway.configurations.UriConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		String cartUri = uriConfiguration.getCartUri();
		String orderUri = uriConfiguration.getOrderUri();

		return builder.routes()
				// Products
				.route(p -> p
						.path("/products/**")
						.uri("forward:/products"))
				// Cart
				.route(p -> p
						.path("/cart/add")
						.and().method(HttpMethod.POST)
						.uri("forward:/cart"))
				.route(p -> p
						.path("/cart/remove")
						.and().method(HttpMethod.POST)
						.uri("forward:/cart"))
				.route(p -> p
						.path("/cart")
						.and().method(HttpMethod.GET)
						.uri("forward:/cart"))
				.route(p -> p
						.path("/cart/**")
						.uri(cartUri + "/cart"))
				// Orders
				.route(p -> p
						.path("/orders/{id}/products")
						.and().method(HttpMethod.GET)
						.uri("forward:/orders"))
				.route(p -> p
						.path("/orders")
						.and().method(HttpMethod.GET)
						.uri("forward:/orders"))
				.route(p -> p
						.path("/orders/{id}")
						.and().method(HttpMethod.GET)
						.uri("forward:/orders"))
				.route(p -> p
						.path("/orders/**")
						.uri(orderUri + "/orders"))
				.build();
	}
}
