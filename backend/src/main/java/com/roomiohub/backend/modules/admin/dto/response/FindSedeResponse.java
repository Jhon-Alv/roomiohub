package com.roomiohub.backend.modules.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindSedeResponse {
    private UUID id;
    private String nombre;
    private String direccion;
    private Boolean activo;
}
