package com.roomiohub.backend.modules.booking.api;

import java.util.UUID;

public interface IMetricsReservaPort {
    IngresosResponse sumIngresos(UUID sedeId);
    IngresosDiariosResponse getIngresosDiarios(UUID sedeId, int dias);
}
