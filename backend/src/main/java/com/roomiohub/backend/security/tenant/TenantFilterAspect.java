package com.roomiohub.backend.security.tenant;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantFilterAspect {

    private final EntityManager entityManager;

    @Before("execution(* com.roomiohub.backend..repository..*(..))")
    public void applyTenantFilter() {

        UUID tenantId = TenantContextHolder.getTenantId();

        if (tenantId == null){
            log.debug("Sin tenantId en contexto, omitiendo filtro (endpoint público)");
            return;
        }

        Session session = entityManager.unwrap(Session.class);

        if (!session.isOpen()) {
            return;
        }

        session.enableFilter("tenantFilter")
                .setParameter("tenantId", tenantId);

        log.debug("Filtro tenant activado: {}", tenantId);

    }

}
