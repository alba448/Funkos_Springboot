package example.demo.categoria.service;

import example.demo.categoria.dto.CategoriaDto;
import example.demo.categoria.model.Categoria;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoriaService {
    List<Categoria> getAll();
    Categoria getById(String id);
    Categoria getByNombre(String nombre);
    Categoria save(CategoriaDto categoriaDto);
    Categoria update(String id, CategoriaDto categoriaDto);
    Categoria delete(String id, CategoriaDto categoriaDto);
}