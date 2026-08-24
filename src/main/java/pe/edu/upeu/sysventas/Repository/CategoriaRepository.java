package pe.edu.upeu.sysventas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.sysventas.Model.Categoria;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, long id);

    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

}
