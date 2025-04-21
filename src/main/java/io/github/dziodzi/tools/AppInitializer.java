package io.github.dziodzi.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppInitializer {

    @Value("${custom.my-app.port}")
    private int port;

    @Value("${custom.my-app.address}")
    private String address;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUrl;

    @Value("${springdoc.api-docs.path}")
    private String apiUrl;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Swagger UI is available at: http://{}:{}{}", address, port, swaggerUrl);
        log.info("API documentation is available at: http://{}:{}{}", address, port, apiUrl);
    }
}