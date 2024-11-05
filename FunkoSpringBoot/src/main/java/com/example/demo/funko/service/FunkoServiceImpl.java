package com.example.demo.funko.service;


import com.example.demo.funko.exceptions.FunkoNotFound;
import com.example.demo.funko.model.Funko;
import com.example.demo.funko.repository.FunkoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@CacheConfig(cacheNames = {"funkos"})

@Service
public class FunkoServiceImpl implements FunkoService {
    private final Logger logger = LoggerFactory.getLogger(FunkoServiceImpl.class);

    private final FunkoRepository funkoRepository;

    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository) {
        this.funkoRepository = funkoRepository;
    }

    @Override
    public List<Funko> getAll() {
        logger.info("Obteniendo funkos");
        return funkoRepository.findAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Funko getById(Long id) {
        logger.info("Obteniendo funko por id: {}", id);
        return funkoRepository.findById(id).orElseThrow(() -> new FunkoNotFound(id));
    }

    @Override
    @CachePut(key = "#result.id")
    public Funko create(Funko funko) {
        logger.info("Creando funko");
        return funkoRepository.save(funko);
    }

    @Override
    @CachePut(key = "#result.id")
    //@CacheEvict(key = "#id")
    public Funko update(Long id, Funko funko) {
        logger.info("Actualizando funko con id: {}", id);
        var res = funkoRepository.findById(id).orElseThrow(() -> new FunkoNotFound(id));
        res.setNombre(funko.getNombre());
        res.setPrecio(funko.getPrecio());
        res.setUpdatedAt(LocalDateTime.now());
        return funkoRepository.save(res);
    }

    @Override
    @CacheEvict(key = "#id")
    public Funko delete(Long id) {
        logger.info("Eliminando funko con id: {}", id);
        Funko funko = funkoRepository.findById(id).orElseThrow(() -> new FunkoNotFound(id));
        funkoRepository.deleteById(id);
        return funko;
    }

    @Override
    public List<Funko> getAllByName(String nombre) {
        logger.info("Obteniendo funko por nombre: {}", nombre);
        return funkoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
