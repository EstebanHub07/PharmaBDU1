package pe.edu.upeu.sysventas.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.auditoria.EventoAuditoria;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.edu.upeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.upeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.upeu.sysventas.exception.RecursoNoEncontradoException;
import pe.edu.upeu.sysventas.exception.ReglaNegocioException;
import pe.edu.upeu.sysventas.model.Categoria;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.repository.CategoriaRepository;
import pe.edu.upeu.sysventas.repository.ProductoRepository;
import pe.edu.upeu.sysventas.service.service.ProductoService;

import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ApplicationEventPublisher eventos;

    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository,
                               ApplicationEventPublisher eventos) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.eventos = eventos;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "productos", allEntries = true)
    public ProductoResponseDTO create(ProductoRequestDTO t) {
        String nombre = t.getNombre().trim();
        if (productoRepository.existsByNombreIgnoreCase(nombre)){
            throw new ReglaNegocioException("Ya existe un producto con el nombre " + nombre);
        }
        Categoria categoria = categoriaRepository.findById(t.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + t.getCategoriaId()
                ));
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(t.getDescripcion());
        producto.setEstado(t.getEstado());
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());
        producto.setCategoria(categoria);

        Producto ProdCreada = productoRepository.save(producto);
        return convertirResponse(ProdCreada);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "productos", allEntries = true)
    public ProductoResponseDTO update(Long aLong, ProductoRequestDTO t) {
        Producto producto = productoRepository.findById(aLong).orElseThrow(() ->
                new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + aLong
                )
        );
        Categoria categoria = categoriaRepository.findById(t.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con id: " + t.getCategoriaId()
                ));
        BigDecimal precioAnterior = producto.getPrecio();
        producto.setNombre(t.getNombre());
        producto.setDescripcion(t.getDescripcion());
        producto.setEstado(t.getEstado());
        producto.setPrecio(t.getPrecio());
        producto.setStock(t.getStock());
        producto.setCategoria(categoria);
        Producto prodActualizada = productoRepository.save(producto);
        if (precioAnterior.compareTo(producto.getPrecio()) != 0) {
            eventos.publishEvent(EventoAuditoria.de("PRECIO_MODIFICADO", "Producto",
                    producto.getId(), precioAnterior + " -> " + producto.getPrecio()));
        }
        return convertirResponse(prodActualizada);

    }
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "productos", key = "'id-' + #aLong")
    public ProductoResponseDTO read (Long aLong){
        Producto producto = productoRepository.findById(aLong)
                .orElseThrow(()->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado con id: " + aLong
                        )
                );
        return convertirResponse(producto);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "productos", allEntries = true)
    public void delete (Long aLong){
        Producto producto = productoRepository.findById(aLong).orElseThrow(()->
                new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + aLong
                )
        );
        productoRepository.delete(producto);
        eventos.publishEvent(EventoAuditoria.de("PRODUCTO_ELIMINADO", "Producto",
                producto.getId(), producto.getNombre()));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "productos", key = "'todas'")
    public Iterable<ProductoResponseDTO> readAll () {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "productos", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public Page<ProductoResponseDTO> readPage(Pageable pageable) {
        return productoRepository.findAll(pageable).map(this::convertirResponse);
    }

    private ProductoResponseDTO convertirResponse(Producto producto){
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
                producto.getFechaModificacion(),
                producto.getCreadoPor(),
                producto.getModificadoPor()
        );
    }
}
