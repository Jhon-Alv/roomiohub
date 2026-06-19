package com.roomiohub.backend.modules.booking.mapper;

import com.roomiohub.backend.modules.booking.dto.response.CheckInResponse;
import com.roomiohub.backend.modules.booking.dto.response.CheckOutResponse;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservaMapper {

    CheckInResponse toCheckInResponse(ReservaEntity entity);

    CheckOutResponse toCheckOutResponse(ReservaEntity entity);
}
