package com.roomiohub.backend.modules.master.dto.promocion.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CreatePromocionResponse {
    private UUID id;
    private String nombre;
}
