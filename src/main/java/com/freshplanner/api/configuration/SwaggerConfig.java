package com.freshplanner.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    /**
     * <h2>Swagger Configuration ({@link DocumentationType#SWAGGER_2})</h2>
     * <ol>
     *     <li>Add custom {@link SwaggerConfig#apiInfo()}.</li>
     *     <li>Security Add-On: Add default {@link SwaggerConfig#securityContext()}.</li>
     *     <li>Security Add-On: Add JWT as {@link SwaggerConfig#apiKey()}.</li>
     *     <li>Select all controller from the base package with all paths.</li>
     * </ol>
     *
     * @return {@link Docket} for the swagger page.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.freshplanner.api.controller"))
                //.apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    // =================================================================================================================

    /**
     * <h2>Security Context Configuration</h2>
     * <ol>
     *     <li>Create the {@link AuthorizationScope} to access everything.</li>
     *     <li>Add the scope(s) into a {@link SecurityReference}.</li>
     *     <li>Build the {@link SecurityContext} out of the reference(s).</li>
     * </ol>
     *
     * @return {@link SecurityContext} with default auth for JWT
     */
    private SecurityContext securityContext() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope("global", "accessEverything")};
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("JWT", authorizationScopes)))
                .build();
    }

    /**
     * <h2>Security Api Key Configuration</h2>
     * Define the {@link ApiKey} to include as an authorization header.
     *
     * @return the {@link ApiKey} with JWT as authorization header.
     */
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    /**
     * <h2>General Api Info Configuration</h2>
     * This {@link ApiInfo} has no functional usage. It displays customized information on top of the page.
     *
     * @return customized {@link ApiInfo} for api description.
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Fresh Planner API",
                "Free time project.\n\n" +
                        "**Information for Authorization:**\n" +
                        "Use the Auth-Controller with the path 'api/auth/login' to get a token as response. Copy this token into the **Authorize-Interaction**.",
                "1.0",
                "Terms of service",
                new Contact("Felix Steinke", "https://github.com/felixsteinke", "steinke.felix@yahoo.de"),
                "Apache 2.0 (default)",
                "https://www.apache.org/licenses/LICENSE-2.0.html",
                Collections.emptyList());
    }
}
