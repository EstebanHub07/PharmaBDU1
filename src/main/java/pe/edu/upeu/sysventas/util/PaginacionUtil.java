package pe.edu.upeu.sysventas.util;

import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pe.edu.upeu.sysventas.exception.ReglaNegocioException;

/** Valida los parámetros de paginación y construye el Pageable (lista blanca de campos). */
public final class PaginacionUtil {

    public static final int TAMANIO_MAXIMO = 100;

    private PaginacionUtil() {
    }

    public static Pageable construir(int pagina, int tamanio, String ordenarPor,
                                     String direccion, Set<String> camposPermitidos,
                                     String campoPorDefecto) {
        if (pagina < 0) {
            throw new ReglaNegocioException("La página no puede ser negativa");
        }
        if (tamanio < 1 || tamanio > TAMANIO_MAXIMO) {
            throw new ReglaNegocioException(
                    "El tamaño de página debe estar entre 1 y " + TAMANIO_MAXIMO);
        }
        String campo = ordenarPor == null || ordenarPor.isBlank()
                ? campoPorDefecto : ordenarPor.trim();
        if (!camposPermitidos.contains(campo)) {
            throw new ReglaNegocioException("El campo de ordenamiento '" + campo
                    + "' no está permitido. Campos válidos: " + camposPermitidos);
        }
        String sentido = direccion == null || direccion.isBlank() ? "asc" : direccion.trim();
        if (!sentido.equalsIgnoreCase("asc") && !sentido.equalsIgnoreCase("desc")) {
            throw new ReglaNegocioException("La dirección de ordenamiento '" + sentido
                    + "' no está permitida. Valores válidos: asc, desc");
        }
        Sort.Direction dir = sentido.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(pagina, tamanio, Sort.by(dir, campo));
    }
}
