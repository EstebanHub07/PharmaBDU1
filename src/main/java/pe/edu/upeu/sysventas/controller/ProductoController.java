package pe.edu.upeu.sysventas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.upeu.sysventas.dto.PaginaResponseDTO;
import pe.edu.upeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.exception.RecursoNoEncontradoException;
import pe.edu.upeu.sysventas.service.service.ProductoService;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<ProductoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam(defaultValue = "nombre") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direccion) {
        if (pagina < 0 || tamanio < 1 || tamanio > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paginación inválida");
        }

        String campoOrden = switch (ordenarPor) {
            case "id", "nombre", "precio", "stock" -> ordenarPor;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de orden inválido");
        };
        Sort.Direction sentido;
        try {
            sentido = Sort.Direction.fromString(direccion);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dirección de orden inválida");
        }

        Page<ProductoResponseDTO> resultado = productoService.readPage(
                PageRequest.of(pagina, tamanio, Sort.by(sentido, campoOrden))
        );
        return ResponseEntity.ok(new PaginaResponseDTO<>(
                resultado.getContent(),
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.isLast()
        ));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(productoService.read(id)
        );
    }
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> create(@Valid @RequestBody ProductoRequestDTO requestDTO){
        ProductoResponseDTO response = productoService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO){
        ProductoResponseDTO response = productoService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductoRequestDTO> delete(
            @PathVariable Long id){
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
