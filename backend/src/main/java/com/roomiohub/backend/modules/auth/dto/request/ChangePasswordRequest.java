package com.roomiohub.backend.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 5, message = "La contraseña debe tener al menos '{min}' caracteres")
    private String newPassword;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String confirmPassword;

}
