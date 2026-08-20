package pe.edu.upeu.sysventas.Service.Impl;

import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.Dto.CategoriaRequestDTO;
import pe.edu.upeu.sysventas.Dto.CategoriaResponseDTO;
import pe.edu.upeu.sysventas.Exception.RecursoNoEncontradoException;
import pe.edu.upeu.sysventas.Exception.ReglaNegocioException;
import pe.edu.upeu.sysventas.Model.Categoria;
import pe.edu.upeu.sysventas.Repository.CategoriaRepository;
import pe.edu.upeu.sysventas.Service.Service.CategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }


    @Transactional
    @Override
    public CategoriaResponseDTO create(CategoriaRequestDTO t) {
        String nombre = t.getNombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("El nombre existe en el sistema"+ nombre);
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(t.getDescripcion());
        categoria.setEstado(t.getEstado());
        Categoria catCreada = categoriaRepository.save(categoria);
        return convertirResponse(catCreada);
    }

    @Transactional
    @Override
    public CategoriaResponseDTO update(Long aLong, CategoriaRequestDTO t) {
        Categoria categoria = categoriaRepository.findById(aLong).orElseThrow(()->new RecursoNoEncontradoException(
                        "Categoria no encontrada con el id: "+aLong
                )
        );
        categoria.setNombre(t.getNombre());
        categoria.setDescripcion(t.getDescripcion());
        categoria.setEstado(t.getEstado());
        Categoria catActualizada = categoriaRepository.save(categoria);
        return convertirResponse(catActualizada);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CategoriaResponseDTO> read(Long aLong) {
        return categoriaRepository.findById(aLong).map(this::convertirResponse);
    }

    @Transactional
    @Override
    public void delete(Long aLong) {
        Categoria categoria = categoriaRepository.findById(aLong).orElseThrow(()->
                new RecursoNoEncontradoException(
                        "Categoria no encontrada con el id: "+aLong
                )
        );

    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<CategoriaResponseDTO> readAll() {
        return categoriaRepository.findAll().stream().map(this::convertirResponse).toList();
    }

    private CategoriaResponseDTO convertirResponse(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado(),
                categoria.getFechaCreacion(),
                categoria.getFechaModificacion()
        );
    }
}
