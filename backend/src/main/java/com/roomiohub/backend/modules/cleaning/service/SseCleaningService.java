package com.roomiohub.backend.modules.cleaning.service;

import com.roomiohub.backend.modules.cleaning.dto.CleaningRoomDto;
import com.roomiohub.backend.modules.cleaning.mapper.CleaningMapper;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.api.habitacion.IMasterHabitacionPort;
import com.roomiohub.backend.modules.master.model.LogEstadoHabitacionEntity;
import com.roomiohub.backend.modules.master.repository.LogEstadoHabitacionRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import com.roomiohub.backend.shared.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseCleaningService {

    private static final Set<EstadoHabitacion> ESTADOS_CLEANING = Set.of(
            EstadoHabitacion.SUCIO, EstadoHabitacion.EN_LIMPIEZA, EstadoHabitacion.OCUPADO);

    private final SseService sseService;
    private final IMasterHabitacionPort masterHabitacionPort;
    private final CleaningMapper cleaningMapper;
    private final LogEstadoHabitacionRepository logEstadoHabitacionRepository;

    @Transactional(readOnly = true)
    public SseEmitter subscribeToCleaning(UUID sedeId) {
        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        log.info("Nuevo cliente suscrito a cleaning SSE sedeId={}, tenantId={}", sedeId, tenantId);

        SseEmitter emitter = sseService.subscribe(sedeId);

        List<HabitacionDataDto> todas = masterHabitacionPort.getHabitacionesActivasBySede(tenantId, sedeId);
        List<HabitacionDataDto> filtradas = todas.stream()
                .filter(h -> ESTADOS_CLEANING.contains(h.getEstadoActual()))
                .collect(Collectors.toList());

        List<CleaningRoomDto> cleaningRooms = buildCleaningRooms(filtradas, sedeId);

        try {
            emitter.send(SseEmitter.event().name("INIT").data(cleaningRooms));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void schedulePublishAfterCommit(UUID sedeId, CleaningRoomDto cleaningRoomDto) {
        sseService.schedulePublishAfterCommit(sedeId, "UPDATE", cleaningRoomDto);
    }

    private List<CleaningRoomDto> buildCleaningRooms(List<HabitacionDataDto> habitaciones, UUID sedeId) {
        List<UUID> ids = habitaciones.stream().map(HabitacionDataDto::getId).collect(Collectors.toList());

        Map<UUID, LocalDateTime> ultimosCambios = logEstadoHabitacionRepository
                .findUltimoCambioByHabitacionIds(sedeId, ids)
                .stream()
                .collect(Collectors.toMap(
                        LogEstadoHabitacionEntity::getHabitacionId,
                        LogEstadoHabitacionEntity::getFechaCambio,
                        (a, b) -> a.isAfter(b) ? a : b));

        return habitaciones.stream()
                .map(h -> {
                    CleaningRoomDto dto = cleaningMapper.toDto(h);
                    dto.setFechaUltimoCambio(ultimosCambios.getOrDefault(h.getId(), null));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
