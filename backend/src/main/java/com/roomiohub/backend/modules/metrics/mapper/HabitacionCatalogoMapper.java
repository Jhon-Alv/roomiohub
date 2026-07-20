package com.roomiohub.backend.modules.metrics.mapper;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.metrics.dto.habitacion.response.HabitacionCatalogoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HabitacionCatalogoMapper {

    @Mapping(target = "categoria", source = "categoriaNombre")
    HabitacionCatalogoItem toItem(HabitacionDataDto source);

    List<HabitacionCatalogoItem> toItemList(List<HabitacionDataDto> source);
}
