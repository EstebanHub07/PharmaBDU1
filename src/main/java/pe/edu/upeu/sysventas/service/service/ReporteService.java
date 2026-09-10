package pe.edu.upeu.sysventas.service.service;

import pe.edu.upeu.sysventas.dto.report.ProductoMasVendidoDTO;
import pe.edu.upeu.sysventas.dto.report.VentaPorCategoriaDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    List<VentaPorCategoriaDTO> ventasPorCategoria(
            LocalDate desde,
            LocalDate hasta);

    List<ProductoMasVendidoDTO> productosMasVendidos(
            LocalDate desde,
            LocalDate hasta);
}
