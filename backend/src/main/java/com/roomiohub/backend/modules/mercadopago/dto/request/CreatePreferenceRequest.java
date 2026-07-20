package com.roomiohub.backend.modules.mercadopago.dto.request;

import com.roomiohub.backend.shared.validators.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePreferenceRequest {

    @NotNull(message = "El planId es obligatorio")
    @ValidUUID(message = "El ID del plan debe ser un UUID valido")
    private String planId;

    @NotBlank(message = "El nombre comercial es obligatorio")
    @Size(max = 255)
    private String nombreComercial;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 11, max = 20)
    private String ruc;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 100)
    private String username;

}
