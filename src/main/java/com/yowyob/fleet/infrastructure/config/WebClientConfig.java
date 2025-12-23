package com.yowyob.fleet.infrastructure.config;

import com.yowyob.fleet.infrastructure.adapters.outbound.external.client.StockApiClient;
import com.yowyob.fleet.infrastructure.adapters.outbound.external.client.VehicleApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.yowyob.fleet.infrastructure.adapters.outbound.external.client.AuthApiClient;
import com.yowyob.fleet.infrastructure.adapters.outbound.external.client.StockApiClient;

@Configuration
public class WebClientConfig {

    /**
     * Configures the client for the external Stock Service.
     */
    @Bean
    public StockApiClient stockApiClient(WebClient.Builder builder, 
                                         @Value("${application.external.stock-service-url}") String url) {
                                            
        WebClient webClient = builder.baseUrl(url).build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        
        return factory.createClient(StockApiClient.class);
    }

    /**
     * Configures the client for the external Vehicle Service.
     */
    @Bean
    public VehicleApiClient vehicleApiClient(WebClient.Builder builder, 
                                             @Value("${application.external.vehicle-service-url}") String url) {
                                            
        WebClient webClient = builder.baseUrl(url).build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        
        return factory.createClient(VehicleApiClient.class);
    }
    @Bean
    public AuthApiClient authApiClient(WebClient.Builder builder, 
                                   @Value("${application.auth.url}") String url) {
    WebClient webClient = builder.baseUrl(url).build();
    return HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build()
            .createClient(AuthApiClient.class);
}
}