package com.roomiohub.backend.modules.booking.model;

import com.roomiohub.backend.shared.persistence.TenantBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservas")
public class ReservaEntity extends TenantBaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sede_id", nullable = false)
    private UUID sedeId;

    @Column(name = "habitacion_id", nullable = false)
    private UUID habitacionId;

    @Column(name = "usuario_ingreso_id", nullable = false)
    private UUID usuarioIngresoId;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "tarifa_id", nullable = false)
    private UUID tarifaId;

    @Column(name = "promocion_id")
    private UUID promocionId;

    @Builder.Default
    @Column(name = "cantidad_huespedes")
    private Integer cantidadHuespedes = 1;

    @Column(name = "metodo_pago", columnDefinition = "metodo_pago")
    private String metodoPago;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_salida_esperada", nullable = false)
    private LocalDateTime fechaSalidaEsperada;

    @Column(name = "fecha_salida_real")
    private LocalDateTime fechaSalidaReal;

    @Column(name = "monto_subtotal", nullable = false)
    private BigDecimal montoSubtotal;

    @Column(name = "monto_descuento")
    private BigDecimal montoDescuento;

    @Column(name = "monto_penalidad")
    private BigDecimal montoPenalidad;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal;
}
