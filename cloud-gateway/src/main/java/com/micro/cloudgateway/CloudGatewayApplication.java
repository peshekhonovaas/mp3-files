package com.micro.cloudgateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudGatewayApplication {
	@Value("${gateway.route.resource-service.path}")
	private String resourceServicePath;

	@Value("${gateway.route.resource-service.uri}")
	private String resourceServiceUri;

	@Value("${gateway.route.resource-processor.path}")
	private String resourceProcessorPath;

	@Value("${gateway.route.resource-processor.uri}")
	private String resourceProcessorUri;

	@Value("${gateway.route.song-service.path}")
	private String songServicePath;

	@Value("${gateway.route.song-service.uri}")
	private String songServiceUri;

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator songLocator(RouteLocatorBuilder builder) {
		System.out.println("Configuring routes for the gateway...");
		return builder.routes()
				.route(r -> r.path(this.resourceServicePath).uri(this.resourceServiceUri))
				.route(r -> r.path(this.songServicePath).uri(this.songServiceUri))
				.build();
	}

}
