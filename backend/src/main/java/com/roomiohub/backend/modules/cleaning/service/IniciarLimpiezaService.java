package com.roomiohub.backend.modules.cleaning.service;

import com.roomiohub.backend.modules.booking.service.SseMapService;
import com.roomiohub.backend.modules.cleaning.dto.CleaningRoomDto;
import com.roomiohub.backend.modules.cleaning.dto.request.IniciarLimpiezaRequest;
import com.roomiohub.backend.modules.cleaning.dto.response.IniciarLimpiezaResponse;
import com.roomiohub.backend.modules.cleaning.mapper.CleaningMapper;
import com.roomiohub.backend.modules.cleaning.mapper.IniciarLimpiezaMapper;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.api.habitacion.MasterHabitacionPort;
import com.roomiohub.backend.modules.master.model.LogEstadoHabitacionEntity;
import com.roomiohub.backend.modules.master.repository.LogEstadoHabitacionRepository;
import com.roomiohub.backend.security.identity.UserContextHolder;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.dto.RoomMapDto;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import com.roomiohub.backend.shared.exceptions.RestException;
import com.roomiohub.backend.shared.mapper.RoomMapMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class IniciarLimpiezaService {

    private final MasterHabitacionPort masterHabitacionPort;
    private final SseMapService sseMapService;
    private final SseCleaningService sseCleaningService;
    private final RoomMapMapper roomMapMapper;
    private final CleaningMapper cleaningMapper;
    private final IniciarLimpiezaMapper iniciarLimpiezaMapper;
    private final LogEstadoHabitacionRepository logEstadoHabitacionRepository;

    @Transactional
    public IniciarLimpiezaResponse execute(UUID habitacionId, IniciarLimpiezaRequest request) {

        UUID sedeId = TenantContextHolder.resolveSedeId(request.getSedeId());
        UUID usuarioId = UserContextHolder.getRequiredUserId();

        HabitacionDataDto habitacion = masterHabitacionPort.getHabitacionById(habitacionId);
        EstadoHabitacion estadoAnterior = habitacion.getEstadoActual();

        if (EstadoHabitacion.SUCIO != estadoAnterior) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Solo se puede iniciar limpieza en habitaciones en estado SUCIO");
        }

        masterHabitacionPort.updateEstadoHabitacion(habitacionId, EstadoHabitacion.EN_LIMPIEZA, usuarioId, request.getObservacion());

        habitacion.setEstadoActual(EstadoHabitacion.EN_LIMPIEZA);
        RoomMapDto roomMapDto = roomMapMapper.toDto(habitacion);
        sseMapService.schedulePublishAfterCommit(sedeId, roomMapDto);

        CleaningRoomDto cleaningRoomDto = cleaningMapper.toDto(habitacion);
        cleaningRoomDto.setFechaUltimoCambio(logEstadoHabitacionRepository
                .findFirstByHabitacionIdOrderByFechaCambioDesc(habitacionId)
                .map(LogEstadoHabitacionEntity::getFechaCambio)
                .orElse(null));
        sseCleaningService.schedulePublishAfterCommit(sedeId, cleaningRoomDto);

        return iniciarLimpiezaMapper.toResponse(habitacionId, estadoAnterior);
    }
}
