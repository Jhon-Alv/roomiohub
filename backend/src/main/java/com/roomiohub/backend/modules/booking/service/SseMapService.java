package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.api.habitacion.IMasterHabitacionPort;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.dto.RoomMapDto;
import com.roomiohub.backend.shared.mapper.RoomMapMapper;
import com.roomiohub.backend.shared.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseMapService {

    private final SseService sseService;
    private final IMasterHabitacionPort masterHabitacionPort;
    private final RoomMapMapper roomMapMapper;

    @Transactional(readOnly = true)
    public SseEmitter subscribeToMap(UUID sedeId) {
        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        log.info("Nuevo cliente suscrito al mapa de sedeId={} para tenantId={}", sedeId, tenantId);

        SseEmitter emitter = sseService.subscribe(sedeId);

        List<HabitacionDataDto> habitaciones = masterHabitacionPort.getHabitacionesActivasBySede(tenantId, sedeId);
        List<RoomMapDto> initialData = roomMapMapper.toDtoList(habitaciones);

        try {
            emitter.send(SseEmitter.event().name("INIT").data(initialData));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void schedulePublishAfterCommit(UUID sedeId, RoomMapDto roomMapDto) {
        sseService.schedulePublishAfterCommit(sedeId, "UPDATE", roomMapDto);
    }
}
