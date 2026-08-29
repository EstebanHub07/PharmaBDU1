package pe.edu.upeu.sysventas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sysventas.Model.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, long id);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}
