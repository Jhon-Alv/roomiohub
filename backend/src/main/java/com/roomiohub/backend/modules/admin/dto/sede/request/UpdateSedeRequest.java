package com.roomiohub.backend.modules.admin.dto.sede.request;

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
public class UpdateSedeRequest {

    @NotBlank(message = "El nombre de la sede es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @Size(max = 500, message = "La dirección no puede superar los 500 caracteres")
    private String direccion;
}
