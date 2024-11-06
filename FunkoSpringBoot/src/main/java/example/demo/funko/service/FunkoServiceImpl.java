package example.demo.funko.service;


import example.demo.categoria.service.CategoriaService;
import example.demo.funko.dto.FunkoDto;
import example.demo.funko.mappers.FunkoMapper;
import example.demo.funko.model.Funko;
import example.demo.funko.repository.FunkoRepository;


import example.demo.funko.validators.FunkoValidator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = {"funkos"})
public class FunkoServiceImpl implements FunkoService{
    private final FunkoRepository repository;
    private final FunkoMapper mapper;
    private final CategoriaService categoriaService;
    private final FunkoValidator validator;

    @Autowired
    public FunkoServiceImpl(FunkoRepository repository, FunkoMapper mapper, CategoriaService categoriaService, FunkoValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoriaService = categoriaService;
        this.validator = validator;
    }

    @Override
    public List<Funko> getAll() {
        log.info("Buscando todos los funkos");
        return repository.findAll();
    }

    @Cacheable
    @Override
    public Funko getById(String id) {
        log.info("Buscando funko con id: {}", id);
        if (!validator.isIdValid(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id no es valido. Debe ser de tipo Long");
        }
        return repository.findById(Long.parseLong(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el funko con id " + id)
        );
    }


    @Cacheable
    @Override
    public Funko getByNombre(String nombre) {
        log.info("Buscando funko llamado: {}", nombre);
        return repository.findByNombre(nombre).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El funko llamado " + nombre + " no existe")
        );
    }

    @CachePut
    @Override
    public Funko save(FunkoDto funkoDto) {
        log.info("Guardando nuevo funko llamado: {}", funkoDto.getNombre());
        var categoria = categoriaService.getByNombre(funkoDto.getCategoria());
        if (!validator.isNameUnique(funkoDto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del funko ya existe");
        }
        return repository.save(mapper.toFunko(funkoDto, categoria));
    }

    @CachePut
    @Override
    public Funko update(String id, FunkoDto funkoDto) {
        log.info("Actualizando funko con id: {}", id);
        if (!validator.isIdValid(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id no es valido. Debe ser de tipo Long");
        }
        var res = repository.findById(Long.parseLong(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el funko con id " + id)
        );
        if (!validator.isNameUnique(funkoDto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del funko ya existe");
        }
        var categoria = categoriaService.getByNombre(funkoDto.getCategoria());
        res.setNombre(funkoDto.getNombre());
        res.setPrecio(funkoDto.getPrecio());
        res.setCategoria(categoria);
        return repository.save(res);
    }

    @CacheEvict
    @Override
    public Funko delete(String id) {
        log.info("Borrando funko con id: {}", id);
        if (!validator.isIdValid(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id no es valido. Debe ser de tipo Long");
        }
        Funko funko = repository.findById(Long.parseLong(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el funko con id " + id)
        );
        repository.deleteById(Long.parseLong(id));
        return funko;
    }
}