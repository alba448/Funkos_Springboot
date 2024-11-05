package com.example.demo.categoria.service;

import com.example.demo.categoria.dto.CategoriaDto;
import com.example.demo.categoria.model.Categoria;
import com.example.demo.funko.model.Funko;

import java.util.List;
import java.util.UUID;

public interface CategoriaService {
    List<Categoria> getAll();

    Categoria getById(UUID id);

    Categoria create(Categoria categoria);

    Categoria update(UUID id, Categoria categoria);

    Categoria delete(UUID id);

    List<Categoria> getByTipo(String tipo);
}
