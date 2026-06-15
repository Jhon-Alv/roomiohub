package com.roomiohub.backend.modules.master.dto.piso.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CreatePisoResponse {
    private UUID id;
    private String nombre;
    private UUID sedeId;
    private String sedeNombre;
}
