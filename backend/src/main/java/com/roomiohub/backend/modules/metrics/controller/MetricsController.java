package com.roomiohub.backend.modules.metrics.controller;

import com.roomiohub.backend.modules.metrics.dto.dashboard.response.DashboardResponse;
import com.roomiohub.backend.modules.metrics.dto.habitacion.response.HabitacionCatalogoItem;
import com.roomiohub.backend.modules.metrics.dto.historial.response.HistorialEstadoResponse;
import com.roomiohub.backend.modules.metrics.dto.ingresos.response.IngresosDiariosResponse;
import com.roomiohub.backend.modules.metrics.service.DashboardService;
import com.roomiohub.backend.modules.metrics.service.HabitacionCatalogoService;
import com.roomiohub.backend.modules.metrics.service.HistorialEstadosService;
import com.roomiohub.backend.modules.metrics.service.IngresosDiariosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final DashboardService dashboardService;
    private final HabitacionCatalogoService habitacionCatalogoService;
    private final HistorialEstadosService historialEstadosService;
    private final IngresosDiariosService ingresosDiariosService;

    @GetMapping("/dashboard/{sedeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable UUID sedeId) {
        return ResponseEntity.ok(dashboardService.execute(sedeId));
    }

    @GetMapping("/catalog/habitaciones/{sedeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HabitacionCatalogoItem>> getCatalogoHabitaciones(@PathVariable UUID sedeId) {
        return ResponseEntity.ok(habitacionCatalogoService.execute(sedeId));
    }

    @GetMapping("/historial-estados/{sedeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<HistorialEstadoResponse>> getHistorialEstados(
            @PathVariable UUID sedeId,
            @RequestParam(required = false) UUID habitacionId,
            @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(historialEstadosService.execute(sedeId, habitacionId, pageable));
    }

    @GetMapping("/ingresos-diarios/{sedeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngresosDiariosResponse> getIngresosDiarios(
            @PathVariable UUID sedeId,
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(ingresosDiariosService.execute(sedeId, dias));
    }
}
