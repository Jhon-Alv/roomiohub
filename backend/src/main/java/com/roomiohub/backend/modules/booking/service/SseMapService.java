package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.booking.dto.response.RoomMapDto;
import com.roomiohub.backend.modules.booking.mapper.RoomMapMapper;
import com.roomiohub.backend.modules.master.api.habitacion.IMasterHabitacionPort;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseMapService {

    private final IMasterHabitacionPort masterHabitacionPort;
    private final RoomMapMapper roomMapMapper;
    private final Map<UUID, List<SseEmitter>> emittersMap = new ConcurrentHashMap<>();

    @Transactional(readOnly = true)
    public SseEmitter subscribeToMap(UUID sedeId) {
        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        log.info("Nuevo cliente suscrito al mapa de sedeId={} para tenantId={}", sedeId, tenantId);

        SseEmitter emitter = new SseEmitter(3600000L);

        List<SseEmitter> sedeEmitters = emittersMap.computeIfAbsent(sedeId, k -> new CopyOnWriteArrayList<>());
        sedeEmitters.add(emitter);

        emitter.onCompletion(() -> sedeEmitters.remove(emitter));
        emitter.onTimeout(() -> sedeEmitters.remove(emitter));
        emitter.onError((e) -> sedeEmitters.remove(emitter));

        List<HabitacionDataDto> habitaciones = masterHabitacionPort.getHabitacionesActivasBySede(tenantId, sedeId);
        List<RoomMapDto> initialData = roomMapMapper.toDtoList(habitaciones);

        try {
            emitter.send(SseEmitter.event().name("INIT").data(initialData));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void publishUpdate(UUID sedeId, RoomMapDto updatedRoom) {
        List<SseEmitter> sedeEmitters = emittersMap.get(sedeId);
        if (sedeEmitters != null) {
            List<SseEmitter> deadEmitters = new ArrayList<>();
            sedeEmitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("UPDATE").data(updatedRoom));
                } catch (IOException e) {
                    deadEmitters.add(emitter);
                }
            });
            sedeEmitters.removeAll(deadEmitters);
        }
    }
}
