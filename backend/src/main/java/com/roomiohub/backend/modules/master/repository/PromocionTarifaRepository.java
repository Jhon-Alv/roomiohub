package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.PromocionTarifaEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromocionTarifaRepository extends BaseJpaRepository<PromocionTarifaEntity, UUID> {

    List<PromocionTarifaEntity> findByPromocionId(UUID promocionId);

    Optional<PromocionTarifaEntity> findByPromocionIdAndId(UUID promocionId, UUID id);

    List<PromocionTarifaEntity> findByPromocionIdAndTarifaIdIn(UUID promocionId, List<UUID> tarifaIds);

    boolean existsByPromocionIdAndTarifaId(UUID promocionId, UUID tarifaId);
}
