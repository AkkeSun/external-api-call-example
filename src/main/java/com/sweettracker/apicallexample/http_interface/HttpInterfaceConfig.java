package com.sweettracker.apicallexample.http_interface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    TestHttpInterface testHttpInterface() {
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader("key", "val")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
            .defaultStatusHandler(HttpStatusCode::isError,
                res -> res.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new RuntimeException(body))))
            .build();

        return HttpServiceProxyFactory.builder()
            .exchangeAdapter(WebClientAdapter.create(webClient))
            .build()
            .createClient(TestHttpInterface.class);
    }
}
