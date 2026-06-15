package com.roomiohub.backend.modules.master.model;

import com.roomiohub.backend.shared.persistence.TenantBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "promociones")
public class PromocionEntity extends TenantBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

}
