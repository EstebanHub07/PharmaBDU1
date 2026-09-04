package pe.edu.upeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sysventas.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}