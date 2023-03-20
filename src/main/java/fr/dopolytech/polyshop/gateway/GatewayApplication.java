package fr.dopolytech.polyshop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		String cartUri = uriConfiguration.getCartUri();
		String orderUri = uriConfiguration.getOrderUri();

		return builder.routes()
				.route(p -> p
						.path("/products/**")
						.uri("forward:/products"))
				.route(p -> p
						.path("/cart/**")
						.uri(cartUri + "/cart"))
				.route(p -> p
						.path("/orders/**")
						.uri(orderUri + "/orders"))
				.build();
	}

}
