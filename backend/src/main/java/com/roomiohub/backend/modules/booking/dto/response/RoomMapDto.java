package com.roomiohub.backend.modules.booking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoomMapDto {
    private UUID id;
    private String numero;
    private String categoria;
    private String piso;
    private String estadoActual;
}
