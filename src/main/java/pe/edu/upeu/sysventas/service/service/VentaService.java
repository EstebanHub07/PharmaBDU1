package pe.edu.upeu.sysventas.service.service;

import pe.edu.upeu.sysventas.dto.VentaRequestDTO;
import pe.edu.upeu.sysventas.dto.VentaResponseDTO;
import pe.edu.upeu.sysventas.enums.EstadoVenta;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {
    VentaResponseDTO registrar(VentaRequestDTO request);
    VentaResponseDTO buscar(Long id);
    List<VentaResponseDTO> listar();
    List<VentaResponseDTO> buscarVentas(
            Long clienteId,
            EstadoVenta estado,
            LocalDate desde,
            LocalDate hasta,
            String ordenarPor,
            String direccion);
}