package com.roomiohub.backend.modules.master.api.tarifa;

import com.roomiohub.backend.modules.master.model.PromocionTarifaEntity;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import com.roomiohub.backend.modules.master.repository.PromocionTarifaRepository;
import com.roomiohub.backend.modules.master.repository.TarifaRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MasterTarifaPort implements IMasterTarifaPort {

    private final TarifaRepository tarifaRepository;
    private final PromocionTarifaRepository promocionTarifaRepository;

    @Override
    @Transactional(readOnly = true)
    public Integer getHorasByTarifaId(UUID tarifaId) {
        TarifaEntity tarifa = tarifaRepository.findById(tarifaId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Tarifa no encontrada"));
        return tarifa.getPlanTiempo().getHoras();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPrecioBaseByTarifaId(UUID tarifaId) {
        TarifaEntity tarifa = tarifaRepository.findById(tarifaId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Tarifa no encontrada"));
        return tarifa.getPrecioBase();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPrecioPromocional(UUID promocionId, UUID tarifaId) {
        return promocionTarifaRepository.findByPromocionIdAndTarifaId(promocionId, tarifaId)
                .map(PromocionTarifaEntity::getPrecioPromocional)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Promoción no válida para esta tarifa"));
    }

    @Override
    @Transactional(readOnly = true)
    public void validateTarifaBelongsToCategoria(UUID tarifaId, UUID categoriaId) {
        TarifaEntity tarifa = tarifaRepository.findById(tarifaId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Tarifa no encontrada"));

        if (!tarifa.getCategoria().getId().equals(categoriaId)) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "La tarifa no aplica a la categoría de la habitación");
        }
    }

}
