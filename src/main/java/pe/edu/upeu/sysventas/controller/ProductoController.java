package pe.edu.upeu.sysventas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.exception.RecursoNoEncontradoException;
import pe.edu.upeu.sysventas.service.service.ProductoService;

@RestController
@RequestMapping("v1/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    @GetMapping
    public ResponseEntity<Iterable<ProductoResponseDTO>> findAll(){
        return ResponseEntity.ok(
                productoService.readAll()
        );
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