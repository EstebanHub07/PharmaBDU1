package pe.edu.upeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sysventas.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}
