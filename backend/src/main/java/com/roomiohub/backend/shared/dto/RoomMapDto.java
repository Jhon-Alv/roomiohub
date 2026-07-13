package com.roomiohub.backend.shared.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoomMapDto {
    private UUID id;
    private String numero;
    private UUID categoriaId;
    private String categoria;
    private UUID pisoId;
    private String piso;
    private String estadoActual;
}
