package com.roomiohub.backend.security.tenant;

import com.roomiohub.backend.shared.exceptions.RestException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class TenantContextHolder {

    private static final ThreadLocal<UUID> TENANT = new InheritableThreadLocal<>();
    private static final ThreadLocal<UUID> SEDE = new InheritableThreadLocal<>();

    public static void setTenantId(UUID tenantId){
        if (tenantId == null) throw new IllegalArgumentException("Tenant ID no puede ser null");
        TENANT.set(tenantId);
    }

    public static UUID getRequiredTenantId() {
        UUID tenantId = TENANT.get();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID no está seteado en el contexto");
        }
        return tenantId;
    }

    public static UUID getTenantId() {
        return TENANT.get();
    }

    public static void setSedeId(UUID sedeId) {
        SEDE.set(sedeId);
    }

    public static UUID getSedeId() {
        return SEDE.get();
    }

    public static UUID resolveSedeId(UUID requestedSedeId) {
        UUID jwtSede = SEDE.get();
        if (jwtSede != null) {
            return jwtSede;
        }
        if (requestedSedeId == null) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "sedeId es obligatorio para usuarios sin sede asignada");
        }
        return requestedSedeId;
    }

    public static void clear(){
        TENANT.remove();
        SEDE.remove();
    }

}
