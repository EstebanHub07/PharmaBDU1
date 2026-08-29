package pe.edu.upeu.sysventas.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysventas.Dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.Dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.Exception.RecursoNoEncontradoException;
import pe.edu.upeu.sysventas.Exception.ReglaNegocioException;
import pe.edu.upeu.sysventas.Model.Categoria;
import pe.edu.upeu.sysventas.Model.Producto;
import pe.edu.upeu.sysventas.Repository.CategoriaRepository;
import pe.edu.upeu.sysventas.Repository.ProductoRepository;
import pe.edu.upeu.sysventas.Service.Service.ProductoService;

import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository; // 1. Inyectar CategoriaRepository

    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    @Override
    public ProductoResponseDTO create(ProductoRequestDTO t) {
        String nombre = validarNombre(t.getNombre());
        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe un producto con el nombre: " + nombre);
        }

        // 2. Buscar la categoría requerida en la Base de Datos
        Categoria categoria = categoriaRepository.findById(t.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con el id: " + t.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(t.getDescripcion());
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());
        producto.setEstado(t.getEstado());
        producto.setCategoria(categoria); // 3. Asignar la categoría al producto

        Producto proCreada = productoRepository.save(producto);
        return convertirResponse(proCreada);
    }

    @Transactional
    @Override
    public ProductoResponseDTO update(Long aLong, ProductoRequestDTO t) {
        String nombre = validarNombre(t.getNombre());
        Producto producto = productoRepository.findById(aLong)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con el id: " + aLong));

        if (productoRepository.existsByNombreIgnoreCaseAndIdNot(nombre, aLong)) {
            throw new ReglaNegocioException("Ya existe otro producto con el nombre: " + nombre);
        }

        // Buscar y actualizar también la categoría en caso haya cambiado
        Categoria categoria = categoriaRepository.findById(t.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con el id: " + t.getCategoriaId()));

        producto.setNombre(nombre);
        producto.setDescripcion(t.getDescripcion());
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());
        producto.setEstado(t.getEstado());
        producto.setCategoria(categoria); // Asignar la categoría

        Producto proActualizada = productoRepository.save(producto);
        return convertirResponse(proActualizada);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProductoResponseDTO> read(Long aLong) {
        return productoRepository.findById(aLong).map(this::convertirResponse);
    }

    @Transactional
    @Override
    public void delete(Long aLong) {
        Producto producto = productoRepository.findById(aLong)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con el id: " + aLong));
        productoRepository.delete(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<ProductoResponseDTO> readAll() {
        return productoRepository.findAll().stream().map(this::convertirResponse).toList();
    }

    private ProductoResponseDTO convertirResponse(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getId() : null,
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getEstado(),
                producto.getFechaCreacion(),
                producto.getFechaModificacion()
        );
    }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del producto no puede estar vacío");
        }
        return nombre.trim();
    }
}