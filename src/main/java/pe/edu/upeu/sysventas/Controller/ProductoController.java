package pe.edu.upeu.sysventas.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysventas.Dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.Dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.Exception.RecursoNoEncontradoException;
import pe.edu.upeu.sysventas.Service.Service.ProductoService;

@RestController
@RequestMapping("v1/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista con todos los productos registrados")
    @ApiResponse(responseCode = "200", description = "Lista devuelta con éxito")
    @GetMapping
    public ResponseEntity<Iterable<ProductoResponseDTO>> findAll() {
        return ResponseEntity.ok(productoService.readAll());
    }

    // 2. BUSCAR POR ID
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto en base a su ID único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> findById(
            @Parameter(description = "ID del producto a buscar", example = "1")
            @PathVariable Long id) {

        ProductoResponseDTO dto = productoService.read(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto con ID " + id + " no existe"));
        return ResponseEntity.ok(dto);
    }

    // 3. CREAR
    @Operation(summary = "Crear nuevo producto", description = "Registra un nuevo producto validando los campos recibidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Bean Validation)")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> create(@Valid @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO response = productoService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 4. ACTUALIZAR
    @Operation(summary = "Actualizar producto existente", description = "Modifica los datos de un producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado para actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> update(
            @Parameter(description = "ID del producto a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO) {
        return ResponseEntity.ok(productoService.update(id, requestDTO));
    }

    // 5. ELIMINAR
    @Operation(summary = "Eliminar producto", description = "Elimina un registro de producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto a eliminar", example = "1")
            @PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}