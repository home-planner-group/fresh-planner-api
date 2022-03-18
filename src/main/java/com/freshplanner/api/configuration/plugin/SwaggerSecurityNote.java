package com.freshplanner.api.configuration.plugin;

import io.swagger.annotations.ApiOperation;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.Optional;

/**
 * Swagger Plugin to add the @PreAuthorize information.
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
public class SwaggerSecurityNote implements OperationBuilderPlugin {

    /**
     * <ol>
     *     <li>Scan for @PreAuthorize and get the value of it.</li>
     *     <li>Scan for existing note in @ApiOperation to add it below.</li>
     *     <li>If note got updated, update the context.</li>
     * </ol>
     *
     * @param context from overwritten method
     */
    @Override
    public void apply(OperationContext context) {
        String notes = "";
        Optional<PreAuthorize> preAuthorize = context.findAnnotation(PreAuthorize.class);
        if (preAuthorize.isPresent()) {
            notes = "**Security @PreAuthorize expression:** `" + preAuthorize.get().value() + "`";
        }
        Optional<ApiOperation> apiOperation = context.findAnnotation(ApiOperation.class);
        if (apiOperation.isPresent() && !apiOperation.get().notes().isEmpty()) {
            if (notes.isEmpty()) {
                notes = apiOperation.get().notes();
            } else {
                notes += "\n\n" + apiOperation.get().notes();
            }
        }
        if (!notes.isEmpty()) {
            context.operationBuilder().notes(notes);
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return SwaggerPluginSupport.pluginDoesApply(documentationType);
    }
}
