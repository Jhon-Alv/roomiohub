package com.roomiohub.backend.modules.booking.api;

import com.roomiohub.backend.modules.booking.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetricsReservaPort implements IMetricsReservaPort {

    private final ReservaRepository reservaRepository;

    @Override
    @Transactional(readOnly = true)
    public IngresosResponse sumIngresos(UUID sedeId) {
        LocalDateTime hoy = LocalDate.now().atStartOfDay();

        List<IngresosPeriodoRow> results = reservaRepository.sumIngresosPeriodos(
                sedeId, hoy, hoy.minusDays(3), hoy.minusDays(7), hoy.minusDays(30));

        IngresosPeriodoRow row = results.isEmpty() ? new IngresosPeriodoRow() : results.get(0);

        return IngresosResponse.builder()
                .hoy(row.getHoy() != null ? row.getHoy() : BigDecimal.ZERO)
                .ultimos3Dias(row.getUltimos3Dias() != null ? row.getUltimos3Dias() : BigDecimal.ZERO)
                .ultimos7Dias(row.getUltimos7Dias() != null ? row.getUltimos7Dias() : BigDecimal.ZERO)
                .ultimos30Dias(row.getUltimos30Dias() != null ? row.getUltimos30Dias() : BigDecimal.ZERO)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public IngresosDiariosResponse getIngresosDiarios(UUID sedeId, int dias) {
        LocalDateTime desde = LocalDate.now().minusDays(dias - 1).atStartOfDay();

        List<Object[]> rows = reservaRepository.sumIngresosAgrupadosPorDia(sedeId, desde);
        List<IngresoDiario> resultado = new ArrayList<>();

        LocalDate fechaActual = desde.toLocalDate();
        LocalDate hoy = LocalDate.now();
        BigDecimal total = BigDecimal.ZERO;

        while (!fechaActual.isAfter(hoy)) {
            BigDecimal montoDia = BigDecimal.ZERO;
            for (Object[] row : rows) {
                if (fechaActual.toString().equals(row[0])) {
                    montoDia = new BigDecimal(row[1].toString());
                    break;
                }
            }
            resultado.add(IngresoDiario.builder().fecha(fechaActual).monto(montoDia).build());
            total = total.add(montoDia);
            fechaActual = fechaActual.plusDays(1);
        }

        return IngresosDiariosResponse.builder().dias(resultado).total(total).build();
    }
}
