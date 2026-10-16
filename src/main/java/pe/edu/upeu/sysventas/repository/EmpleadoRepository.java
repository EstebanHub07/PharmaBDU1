package pe.edu.upeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sysventas.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}