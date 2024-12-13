package rso.iota.authsvc.common.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import rso.iota.authsvc.dto.OutError;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(
        responseCode = "409",
        content = @Content(schema = @Schema(implementation = OutError.class))
)
public @interface Api409 {
    @AliasFor(annotation = ApiResponse.class, attribute = "description")
    String description() default "Conflict";
}
