package org.enset.ebankchatboot.config;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * Resolves MCP server URLs through Eureka.
 *
 * The MCP connections are configured with a service name as host
 * (e.g. http://customer-service). Before each request is sent, this customizer
 * asks Spring Cloud LoadBalancer for a registered instance of that service and
 * rewrites the URI to the instance's real host and port.
 */
@Configuration
public class McpDiscoveryConfig {

    @Bean
    McpSyncHttpClientRequestCustomizer eurekaMcpUriResolver(LoadBalancerClient loadBalancerClient) {
        return (builder, method, uri, body, context) -> {
            String serviceId = uri.getHost();
            ServiceInstance instance = loadBalancerClient.choose(serviceId);
            if (instance == null) {
                throw new IllegalStateException("No instance of '" + serviceId + "' registered in Eureka");
            }
            URI resolved = loadBalancerClient.reconstructURI(instance, uri);
            builder.uri(resolved);
        };
    }
}
