package com.roomiohub.backend.modules.booking.service;

import com.roomiohub.backend.modules.booking.dto.response.FindReservaActivaResponse;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import com.roomiohub.backend.modules.booking.repository.ReservaRepository;
import com.roomiohub.backend.modules.master.api.habitacion.MasterHabitacionPort;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import com.roomiohub.backend.modules.master.repository.TarifaRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindReservaActivaService {

    private final ReservaRepository reservaRepository;
    private final TarifaRepository tarifaRepository;
    private final CategoriaRepository categoriaRepository;
    private final MasterHabitacionPort masterHabitacionPort;

    @Transactional(readOnly = true)
    public FindReservaActivaResponse execute(UUID habitacionId) {

        HabitacionDataDto habitacion = masterHabitacionPort.getHabitacionById(habitacionId);

        if (EstadoHabitacion.OCUPADO != habitacion.getEstadoActual()) {
            throw new RestException(HttpStatus.BAD_REQUEST, "La habitación no está ocupada");
        }

        ReservaEntity reserva = reservaRepository.findByHabitacionIdAndFechaSalidaRealIsNull(habitacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "No hay una reserva activa para esta habitación"));

        TarifaEntity tarifa = tarifaRepository.findById(reserva.getTarifaId())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Tarifa no encontrada"));

        CategoriaEntity categoria = categoriaRepository.findById(habitacion.getCategoriaId())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        return buildResponse(reserva, tarifa, categoria);
    }

    private FindReservaActivaResponse buildResponse(ReservaEntity reserva, TarifaEntity tarifa, CategoriaEntity categoria) {
        BigDecimal precioHoraExtra = categoria.getPrecioHoraExtra();
        Integer planTiempoHoras = tarifa.getPlanTiempo().getHoras();
        BigDecimal penalidadSugerida = calcularPenalidad(reserva.getFechaSalidaEsperada(), precioHoraExtra);

        return FindReservaActivaResponse.builder()
                .id(reserva.getId())
                .habitacionId(reserva.getHabitacionId())
                .clienteId(reserva.getClienteId())
                .tarifaId(reserva.getTarifaId())
                .fechaIngreso(reserva.getFechaIngreso())
                .fechaSalidaEsperada(reserva.getFechaSalidaEsperada())
                .montoSubtotal(reserva.getMontoSubtotal())
                .montoDescuento(reserva.getMontoDescuento())
                .montoPenalidad(reserva.getMontoPenalidad())
                .montoTotal(reserva.getMontoTotal())
                .precioHoraExtra(precioHoraExtra)
                .planTiempoHoras(planTiempoHoras)
                .penalidadSugerida(penalidadSugerida)
                .build();
    }

    private BigDecimal calcularPenalidad(LocalDateTime fechaSalidaEsperada, BigDecimal precioHoraExtra) {
        LocalDateTime ahora = LocalDateTime.now();
        if (!ahora.isAfter(fechaSalidaEsperada) || precioHoraExtra.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        long minutosExcedidos = Duration.between(fechaSalidaEsperada, ahora).toMinutes();
        return precioHoraExtra
                .multiply(BigDecimal.valueOf(minutosExcedidos))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }
}
