package com.roomiohub.backend.modules.metrics.mapper;

import com.roomiohub.backend.modules.booking.api.IngresoDiario;
import com.roomiohub.backend.modules.metrics.dto.ingresos.response.IngresoDiarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IngresosDiariosMapper {

    IngresoDiarioResponse toResponse(IngresoDiario source);

    List<IngresoDiarioResponse> toResponseList(List<IngresoDiario> source);
}
