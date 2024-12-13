package rso.iota.authsvc.common.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rso.iota.authsvc.dto.OutError;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
        {@ApiResponse(
                responseCode = "403",
                description = "User has insufficient rights to access this method.",
                content = @Content(schema = @Schema(implementation = OutError.class))
        ), @ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content = @Content(schema = @Schema(implementation = OutError.class))
        )}
)
@Validated
@CrossOrigin
@RestController
@Tag(
        name = "",
        description = ""
)
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
)
public @interface ControllerCommon {
    @AliasFor(
            annotation = RequestMapping.class,
            attribute = "path"
    ) String path();

    @AliasFor(
            annotation = Tag.class,
            attribute = "name"
    ) String tag();

    @AliasFor(
            annotation = Tag.class,
            attribute = "description"
    ) String description() default "";
}