package example.demo.funko.service;


import example.demo.categoria.service.CategoriaService;
import example.demo.funko.dto.FunkoDto;
import example.demo.funko.mappers.FunkoMapper;
import example.demo.funko.model.Funko;
import example.demo.funko.repository.FunkoRepository;

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
    private FunkoRepository repository;
    private FunkoMapper mapper;
    private CategoriaService categoriaService;

    @Autowired
    public FunkoServiceImpl(FunkoRepository repository, FunkoMapper mapper, CategoriaService categoriaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoriaService = categoriaService;
    }

    @Override
    public List<Funko> getAll() {
        log.info("Buscando todos los funkos");
        return repository.findAll();
    }

    @Cacheable
    @Override
    public Funko getById(Long id) {
        log.info("Buscando funko con id: {}", id);
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el funko con id " + id)
        );
    }

    @CachePut
    @Override
    public Funko save(FunkoDto funkoDto) {
        log.info("Guardando nuevo funko llamado: {}", funkoDto.getNombre());
        var categoria = categoriaService.getByNombre(funkoDto.getCategoria());
        return repository.save(mapper.toFunko(funkoDto, categoria));
    }

    @CachePut
    @Override
    public Funko update(Long id, FunkoDto funkoDto) {
        log.info("Actualizando funko con id: {}", id);
        var res = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el funko con id " + id)
        );
        var categoria = categoriaService.getByNombre(funkoDto.getCategoria());
        res.setNombre(funkoDto.getNombre());
        res.setPrecio(funkoDto.getPrecio());
        res.setCategoria(categoria);
        return repository.save(res);
    }

    @CacheEvict
    @Override
    public Funko delete(Long id) {
        log.info("Borrando funko con id: {}", id);
        Funko funko = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el funko con id " + id)
        );
        repository.deleteFunkoById(id);
        return funko;
    }
}