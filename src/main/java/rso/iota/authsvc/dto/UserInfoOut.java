package rso.iota.authsvc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoOut {
    private String sub;
    private String email;
    private String preferredUsername;
    private String familyName;
    private String givenName;
}
