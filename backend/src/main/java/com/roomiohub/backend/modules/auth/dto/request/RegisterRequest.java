package com.roomiohub.backend.modules.auth.dto.request;

import com.roomiohub.backend.shared.validators.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los '{max}' caracteres")
    private String nombreComercial;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 11, max = 20, message = "El RUC no puede exceder los '{max}' caracteres")
    private String ruc;

    @NotBlank
    @Size(max = 150, message = "El nombre no puede exceder los '{max}' caracteres")
    private String nombre;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 100, message = "El usuario no puede exceder los '{max}' caracteres")
    private String username;

    @NotNull(message = "El planId es obligatorio")
    @ValidUUID(message = "El ID del plan debe ser un UUID válido")
    private String planId;

}
