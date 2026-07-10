package com.roomiohub.backend.modules.master.api.tarifa;

import java.math.BigDecimal;
import java.util.UUID;

public interface IMasterTarifaPort {

    Integer getHorasByTarifaId(UUID tarifaId);

    BigDecimal getPrecioBaseByTarifaId(UUID tarifaId);

    BigDecimal getPrecioPromocional(UUID promocionId, UUID tarifaId);

    void validateTarifaBelongsToCategoria(UUID tarifaId, UUID categoriaId);
}
