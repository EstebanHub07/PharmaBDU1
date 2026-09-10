package pe.edu.upeu.sysventas.dto.report;

import java.math.BigDecimal;

public record VentaPorCategoriaDTO(
        Long categoriaId,
        String categoriaNombre,
        Long cantidadVendida,
        BigDecimal montoTotal
) {
}
