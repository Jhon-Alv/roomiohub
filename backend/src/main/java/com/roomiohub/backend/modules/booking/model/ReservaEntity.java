package com.roomiohub.backend.modules.booking.model;

import com.roomiohub.backend.shared.persistence.TenantBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;

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

    @Enumerated(EnumType.STRING)
    @JdbcType(org.hibernate.dialect.type.PostgreSQLEnumJdbcType.class)
    @Column(name = "metodo_pago", columnDefinition = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

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

    public void iniciarEstadia(Integer horas, BigDecimal precioBase, BigDecimal precioPromocional) {
        this.fechaIngreso = LocalDateTime.now();
        this.fechaSalidaEsperada = this.fechaIngreso.plusHours(horas);
        
        this.montoSubtotal = precioBase;
        if (precioPromocional != null) {
            this.montoDescuento = precioBase.subtract(precioPromocional);
            this.montoTotal = precioPromocional;
        } else {
            this.montoDescuento = BigDecimal.ZERO;
            this.montoTotal = precioBase;
        }
        this.montoPenalidad = BigDecimal.ZERO;
    }

    public void finalizarEstadia(BigDecimal penalidadExtra) {
        this.fechaSalidaReal = LocalDateTime.now();
        if (penalidadExtra != null && penalidadExtra.compareTo(BigDecimal.ZERO) > 0) {
            this.montoPenalidad = (this.montoPenalidad == null ? BigDecimal.ZERO : this.montoPenalidad).add(penalidadExtra);
            this.montoTotal = this.montoTotal.add(penalidadExtra);
        }
    }
}
