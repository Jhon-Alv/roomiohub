package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.booking.dto.request.CheckInRequestDto;
import com.roomiohub.backend.modules.booking.dto.response.RoomMapDto;
import com.roomiohub.backend.modules.booking.mapper.RoomMapMapper;
import com.roomiohub.backend.modules.booking.model.ClienteEntity;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import com.roomiohub.backend.modules.booking.repository.ClienteRepository;
import com.roomiohub.backend.modules.booking.repository.ReservaRepository;
import com.roomiohub.backend.modules.master.api.MasterHabitacionPort;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.security.identity.UserContextHolder;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CheckInService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final MasterHabitacionPort masterHabitacionPort;
    private final SseMapService sseMapService;
    private final RoomMapMapper roomMapMapper;

    @Transactional
    public ReservaEntity execute(UUID habitacionId, CheckInRequestDto dto) {
        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        UUID usuarioId = UserContextHolder.getRequiredUserId();

        ClienteEntity cliente = clienteRepository.findByNumeroDocumentoAndTenantId(dto.getNumeroDocumento(), tenantId)
                .orElseGet(() -> {
                    ClienteEntity nuevoCliente = ClienteEntity.builder()
                            .numeroDocumento(dto.getNumeroDocumento())
                            .nombres(dto.getNombresCliente())
                            .build();
                    return clienteRepository.persist(nuevoCliente);
                });

        ReservaEntity reserva = ReservaEntity.builder()
                .sedeId(dto.getSedeId())
                .habitacionId(habitacionId)
                .usuarioIngresoId(usuarioId)
                .clienteId(cliente.getId())
                .tarifaId(dto.getTarifaId())
                .promocionId(dto.getPromocionId())
                .cantidadHuespedes(dto.getCantidadHuespedes())
                .metodoPago(dto.getMetodoPago())
                .fechaIngreso(LocalDateTime.now())
                .fechaSalidaEsperada(dto.getFechaSalidaEsperada())
                .montoSubtotal(dto.getMontoSubtotal())
                .montoDescuento(dto.getMontoDescuento() != null ? dto.getMontoDescuento() : BigDecimal.ZERO)
                .montoPenalidad(BigDecimal.ZERO)
                .montoTotal(dto.getMontoTotal())
                .build();

        ReservaEntity savedReserva = reservaRepository.persist(reserva);

        masterHabitacionPort.updateEstadoHabitacion(habitacionId, "OCUPADO");

        masterHabitacionPort.getHabitacionesActivasBySede(tenantId, dto.getSedeId()).stream()
                .filter(h -> h.getId().equals(habitacionId))
                .findFirst()
                .ifPresent(habitacionData -> {
                    RoomMapDto roomMapDto = roomMapMapper.toDto(habitacionData);
                    sseMapService.publishUpdate(dto.getSedeId(), roomMapDto);
                });

        return savedReserva;
    }
}
