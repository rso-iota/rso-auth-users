package rso.iota.authsvc.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
public class OutError {
    @NotNull
    @Builder.Default
    private final UUID id = UUID.randomUUID();
    @NotNull
    @Builder.Default
    private final OffsetDateTime time = OffsetDateTime.now();
    @NotNull
    private final String message;
}
