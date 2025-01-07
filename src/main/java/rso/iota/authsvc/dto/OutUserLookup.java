package rso.iota.authsvc.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutUserLookup {
    @NotNull
    private String username;

    @NotNull
    private Boolean me;

    @NotNull
    private String id;
}
