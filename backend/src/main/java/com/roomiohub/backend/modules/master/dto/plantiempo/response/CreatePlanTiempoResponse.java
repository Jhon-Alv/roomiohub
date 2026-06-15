package com.roomiohub.backend.modules.master.dto.plantiempo.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CreatePlanTiempoResponse {
    private UUID id;
    private String nombre;
    private Integer horas;
}
