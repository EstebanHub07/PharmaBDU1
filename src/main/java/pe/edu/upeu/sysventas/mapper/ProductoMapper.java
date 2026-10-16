package pe.edu.upeu.sysventas.mapper;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.model.Producto;

/*
 * Requiere que producto.categoria ya esté cargada (entity graph);
 * no dispara consultas por su cuenta.
 */
@Component
public class ProductoMapper {

    public ProductoResponseDTO toResponse(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getEstado(),
                producto.getFechaCreacion(),
                producto.getFechaModificacion()
        );
    }
}