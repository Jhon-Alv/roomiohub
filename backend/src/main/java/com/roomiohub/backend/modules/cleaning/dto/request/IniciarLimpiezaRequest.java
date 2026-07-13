package com.roomiohub.backend.modules.cleaning.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IniciarLimpiezaRequest {
    @NotNull
    private UUID sedeId;
    private String observacion;
}
