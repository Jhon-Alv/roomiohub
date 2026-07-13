package com.roomiohub.backend.modules.cleaning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CleaningRoomDto {
    private UUID id;
    private String numero;
    private UUID categoriaId;
    private String categoria;
    private UUID pisoId;
    private String piso;
    private String estadoActual;
    private LocalDateTime fechaUltimoCambio;
}
