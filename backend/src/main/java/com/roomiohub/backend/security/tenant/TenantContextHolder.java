package com.roomiohub.backend.security.tenant;

import java.util.UUID;

public class TenantContextHolder {

    private static final ThreadLocal<UUID> CONTEXT = new InheritableThreadLocal<>();

    public static void setTenantId(UUID tenantId){
        if (tenantId == null) throw new IllegalArgumentException("Tenant ID no puede ser null");
        CONTEXT.set(tenantId);
    }

    public static UUID getRequiredTenantId() {
        UUID tenantId = CONTEXT.get();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID no está seteado en el contexto");
        }
        return tenantId;
    }

    public static UUID getTenantId() {
        return CONTEXT.get();
    }

    public static void clear(){
        CONTEXT.remove();
    }

}
