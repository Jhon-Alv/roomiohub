package com.roomiohub.backend.modules.master.dto.promociontarifa.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BatchCreatePromocionTarifaRequest {

    @NotEmpty(message = "Debe proporcionar al menos una tarifa para la promoción")
    @Valid
    private List<PromocionTarifaItemRequest> items;
}
