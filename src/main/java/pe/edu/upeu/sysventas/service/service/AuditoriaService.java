package pe.edu.upeu.sysventas.service.service;

import java.time.LocalDate;
import pe.edu.upeu.sysventas.auditoria.EventoAuditoria;
import pe.edu.upeu.sysventas.dto.AuditoriaEventoDTO;
import pe.edu.upeu.sysventas.dto.PaginaResponseDTO;

public interface AuditoriaService {

    void registrar(EventoAuditoria evento);

    PaginaResponseDTO<AuditoriaEventoDTO> buscar(String usuario, String accion,
                                                 LocalDate desde, LocalDate hasta,
                                                 int pagina, int tamanio);
}
