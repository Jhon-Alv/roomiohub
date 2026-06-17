package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.booking.dto.request.CheckOutRequestDto;
import com.roomiohub.backend.modules.booking.dto.response.RoomMapDto;
import com.roomiohub.backend.modules.booking.mapper.RoomMapMapper;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import com.roomiohub.backend.modules.booking.repository.ReservaRepository;
import com.roomiohub.backend.modules.master.api.MasterHabitacionPort;
import com.roomiohub.backend.shared.exceptions.RestException;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CheckOutService {

    private final ReservaRepository reservaRepository;
    private final MasterHabitacionPort masterHabitacionPort;
    private final SseMapService sseMapService;
    private final RoomMapMapper roomMapMapper;

    @Transactional
    public ReservaEntity execute(UUID habitacionId, CheckOutRequestDto dto) {
        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        ReservaEntity reserva = reservaRepository.findByHabitacionIdAndFechaSalidaRealIsNull(habitacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "No hay una reserva activa para esta habitación"));

        if (!reserva.getSedeId().equals(dto.getSedeId())) {
            throw new RestException(HttpStatus.FORBIDDEN, "La reserva no pertenece a esta sede");
        }

        // 1. Completar la reserva
        reserva.setFechaSalidaReal(LocalDateTime.now());
        
        if (dto.getMontoPenalidad() != null && dto.getMontoPenalidad().compareTo(BigDecimal.ZERO) > 0) {
            reserva.setMontoPenalidad(dto.getMontoPenalidad());
            reserva.setMontoTotal(reserva.getMontoTotal().add(dto.getMontoPenalidad()));
        }

        reservaRepository.update(reserva);

        // 2. Actualizar el estado de la habitación a 'SUCIO'
        masterHabitacionPort.updateEstadoHabitacion(habitacionId, "SUCIO");

        // 3. Emitir el evento SSE con la habitación actualizada
        masterHabitacionPort.getHabitacionesActivasBySede(tenantId, dto.getSedeId()).stream()
                .filter(h -> h.getId().equals(habitacionId))
                .findFirst()
                .ifPresent(habitacionData -> {
                    RoomMapDto roomMapDto = roomMapMapper.toDto(habitacionData);
                    sseMapService.publishUpdate(dto.getSedeId(), roomMapDto);
                });

        return reserva;
    }
}
