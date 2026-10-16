package pe.edu.upeu.sysventas.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sysventas.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}