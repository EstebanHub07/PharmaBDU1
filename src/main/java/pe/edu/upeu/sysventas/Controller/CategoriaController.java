package pe.edu.upeu.sysventas.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysventas.Dto.CategoriaRequestDTO;
import pe.edu.upeu.sysventas.Dto.CategoriaResponseDTO;
import pe.edu.upeu.sysventas.Exception.RecursoNoEncontradoException;
import pe.edu.upeu.sysventas.Service.Service.CategoriaService;

import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {

        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista con todas las categorías registradas")
    @ApiResponse(responseCode = "200", description = "Lista devuelta con éxito")
    @GetMapping
    public ResponseEntity<Iterable<CategoriaResponseDTO>> findAll() {
        return ResponseEntity.ok(categoriaService.readAll());
    }

    // 2. BUSCAR POR ID (CORREGIDO: Sin Optional en el ResponseEntity)
    @Operation(summary = "Obtener categoría por ID", description = "Retorna una categoría en base a su ID único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(
            @Parameter(description = "ID de la categoría a buscar", example = "1")
            @PathVariable Long id) {

        // Desenpaquetamos el Optional para que el GlobalExceptionHandler capture el 404
        CategoriaResponseDTO dto = categoriaService.read(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La categoría con ID " + id + " no existe"));
        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Crear nueva categoría", description = "Registra una nueva categoría validando los campos recibidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Bean Validation)")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@Valid @RequestBody CategoriaRequestDTO requestDTO) {
        CategoriaResponseDTO response = categoriaService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "Actualizar categoría existente", description = "Modifica los datos de una categoría por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada para actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(
            @Parameter(description = "ID de la categoría a actualizar", example = "1")
            @PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO requestDTO) {
        return ResponseEntity.ok(categoriaService.update(id, requestDTO));
    }
    @Operation(summary = "Eliminar categoría", description = "Elimina un registro de categoría por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoría a eliminar", example = "1")
            @PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
