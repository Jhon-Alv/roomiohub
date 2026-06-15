package com.roomiohub.backend.modules.master.dto.promocion.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FindPromocionesResponse {
    private UUID id;
    private String nombre;
    private Boolean activo;
}
