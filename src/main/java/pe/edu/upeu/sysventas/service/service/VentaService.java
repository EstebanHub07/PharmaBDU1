package pe.edu.upeu.sysventas.service.service;

import pe.edu.upeu.sysventas.dto.VentaRequestDTO;
import pe.edu.upeu.sysventas.dto.VentaResponseDTO;

import java.util.List;

public interface VentaService {
    VentaResponseDTO registrar(VentaRequestDTO request);
    VentaResponseDTO buscar(Long id);
    List<VentaResponseDTO> listar();
    VentaResponseDTO anular(Long id);
}