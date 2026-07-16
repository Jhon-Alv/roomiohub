package com.roomiohub.backend.modules.metrics.service;

import com.roomiohub.backend.modules.booking.api.IMetricsReservaPort;
import com.roomiohub.backend.modules.metrics.dto.ingresos.response.IngresosDiariosResponse;
import com.roomiohub.backend.modules.metrics.mapper.IngresosDiariosMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngresosDiariosService {

    private final IMetricsReservaPort metricsReservaPort;
    private final IngresosDiariosMapper ingresosDiariosMapper;

    @Transactional(readOnly = true)
    public IngresosDiariosResponse execute(UUID sedeId, int dias) {
        var portResponse = metricsReservaPort.getIngresosDiarios(sedeId, dias);

        return IngresosDiariosResponse.builder()
                .dias(ingresosDiariosMapper.toResponseList(portResponse.getDias()))
                .total(portResponse.getTotal())
                .build();
    }
}
