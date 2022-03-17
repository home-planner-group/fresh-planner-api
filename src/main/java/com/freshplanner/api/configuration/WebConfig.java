package com.freshplanner.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for the {@link ViewControllerRegistry}.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adds the redirection from the base path ('/') to the Swagger-UI ('/swagger-ui/').
     *
     * @param registry from overwritten method
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui/");
    }
}
