package com.roomiohub.backend.modules.master.api.tarifa;

import java.math.BigDecimal;
import java.util.UUID;

public interface IMasterTarifaPort {

    /**
     * Obtiene la cantidad de horas asociadas al Plan de Tiempo de una Tarifa.
     */
    Integer getHorasByTarifaId(UUID tarifaId);

    BigDecimal getPrecioBaseByTarifaId(UUID tarifaId);

    BigDecimal getPrecioPromocional(UUID promocionId, UUID tarifaId);
}
