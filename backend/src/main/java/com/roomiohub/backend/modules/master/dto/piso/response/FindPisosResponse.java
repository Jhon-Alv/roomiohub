package com.roomiohub.backend.modules.master.dto.piso.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FindPisosResponse {
    private UUID id;
    private String nombre;
    private Boolean activo;
}
