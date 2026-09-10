package pe.edu.upeu.sysventas.dto.report;

import java.math.BigDecimal;

public record ProductoMasVendidoDTO(
        Long productoId,
        String productoNombre,
        String categoriaNombre,
        Long cantidadVendida,
        BigDecimal montoTotal
) {
}
