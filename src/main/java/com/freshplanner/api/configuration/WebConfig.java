package com.freshplanner.api.configuration;

import com.freshplanner.api.enums.RoleName;
import com.freshplanner.api.enums.Unit;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
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

    /**
     * init converter to decode @RequestParam and @PathVariable
     *
     * @param registry from overwritten method
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new UnitConverter());
        registry.addConverter(new RoleNameConverter());
    }

    // === CONVERTER ===================================================================================================

    static class UnitConverter implements Converter<String, Unit> {
        @Override
        public Unit convert(String s) {
            return Unit.decode(s);
        }
    }

    static class RoleNameConverter implements Converter<String, RoleName> {
        @Override
        public RoleName convert(String s) {
            return RoleName.decode(s);
        }
    }
}
