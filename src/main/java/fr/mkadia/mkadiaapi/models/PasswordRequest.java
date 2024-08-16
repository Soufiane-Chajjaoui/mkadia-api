package fr.mkadia.mkadiaapi.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class PasswordRequest {
    @NotEmpty(message = "Current Password is required")
    private String currentPassword;
    @NotEmpty(message = "New Password is required")
    private String newPassword;
    @NotEmpty(message = "Confirm Password is required")
    private String confirmationPassword;
}
