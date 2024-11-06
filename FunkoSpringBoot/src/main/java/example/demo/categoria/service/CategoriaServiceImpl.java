package example.demo.categoria.service;


import example.demo.categoria.dto.CategoriaDto;
import example.demo.categoria.mappers.CategoriaMapper;
import example.demo.categoria.model.Categoria;
import example.demo.categoria.repository.CategoriaRepository;
import example.demo.categoria.validators.CategoriaValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"categorias"})
public class CategoriaServiceImpl implements CategoriaService {
    private CategoriaRepository repository;
    private CategoriaMapper mapper;
    private CategoriaValidator validator;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository repository, CategoriaMapper mapper, CategoriaValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public List<Categoria> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Categoria getById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoria con id " + id + " no se ha encontrado.")
        );
    }

    @Override
    @Cacheable(key = "#nombre")
    public Categoria getByNombre(String nombre) {
        return repository.findByNombre(nombre).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoria " + nombre + " no existe")
        );
    }

    @Override
    @CachePut(key = "#result.id")
    public Categoria save(CategoriaDto categoriaDto) {
        if (!validator.isNameUnique(categoriaDto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoria no es válido.");
        }
        return repository.save(mapper.fromDto(categoriaDto));
    }

    @Override
    @CachePut(key = "#result.id")
    public Categoria update(UUID id, CategoriaDto categoriaDto) {
        System.out.println("Buscando id: " + id);
        var res = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoria con id " + id + " no se ha encontrado.")
        );
        return repository.save(mapper.toCategoria(categoriaDto, res));
    }

    @Override
    @CachePut(key = "#result.id")
    public Categoria delete(UUID id, CategoriaDto categoriaDto) {
        System.out.println("Buscando id: " + id);
        var res = repository.findByIdAndActivadoTrue(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoria con id " + id + " no se ha encontrado.")
        );
        return repository.save(mapper.toCategoria(categoriaDto, res));
    }
}