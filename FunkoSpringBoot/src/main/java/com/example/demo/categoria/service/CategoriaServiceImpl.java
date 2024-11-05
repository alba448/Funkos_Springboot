package com.example.demo.categoria.service;

import com.example.demo.categoria.exceptions.CategoriaNotFound;
import com.example.demo.categoria.model.Categoria;
import com.example.demo.categoria.repository.CategoriaRepository;

import com.example.demo.funko.exceptions.FunkoNotFound;

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
import java.util.UUID;

@CacheConfig(cacheNames = {"categorias"})

@Service
public class CategoriaServiceImpl implements CategoriaService {
    private final Logger logger = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> getAll() {
        logger.info("Obteniendo funkos");
        return categoriaRepository.findAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Categoria getById(UUID id) {
        logger.info("Obteniendo categoria por id: {}", id);
        return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFound(id));
    }

    @Override
    @CachePut(key = "#result.id")
    public Categoria create(Categoria categoria) {
        logger.info("Creando funko");
        return categoriaRepository.save(categoria);
    }

    @Override
    @CachePut(key = "#result.id")
    public Categoria update(UUID id, Categoria categoria) {
        logger.info("Actualizando categoria con id: {}", id);
        var res = categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFound(id));
        res.setTipo(categoria.getTipo());
        res.setUpdatedAt(LocalDateTime.now());
        return categoriaRepository.save(res);
    }

    @Override
    @CacheEvict(key = "#id")
    public Categoria delete(UUID id) {
        logger.info("Eliminando categoria con id: {}", id);
        Categoria categoria = categoriaRepository.findByIdAndActivaTrue(id).orElseThrow(() -> new CategoriaNotFound(id));
        categoriaRepository.deleteById(id);
        return categoriaRepository.save(categoria);
    }

    @Override
    public List<Categoria> getByTipo(String tipo) {
        logger.info("Obteniendo categoria por tipo: {}", tipo);
        return categoriaRepository.findByTipo(tipo);
    }
}