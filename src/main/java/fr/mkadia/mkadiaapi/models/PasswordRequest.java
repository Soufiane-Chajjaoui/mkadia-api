package fr.mkadia.mkadiaapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordRequest {
    private String email;
//    @NotEmpty(message = "Current Password is required")
    private String currentPassword;
//    @NotEmpty(message = "New Password is required")
    private String newPassword;
//    @NotEmpty(message = "Confirm Password is required")
    private String confirmationPassword;
}
