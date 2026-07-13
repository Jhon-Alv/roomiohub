package com.roomiohub.backend.shared.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class TenantBaseEntity extends TenantEntity {

    @Column(name = "activo", insertable = false)
    private Boolean activo = Boolean.TRUE;
}
