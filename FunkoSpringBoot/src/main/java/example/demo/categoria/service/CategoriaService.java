package example.demo.categoria.service;

import example.demo.categoria.dto.CategoriaDto;
import example.demo.categoria.model.Categoria;

import example.demo.categoria.model.TipoCategoria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoriaService {
    List<Categoria> getAll();
    Categoria getById(UUID id);
    Categoria getByNombre(TipoCategoria nombre);
    Categoria save(CategoriaDto categoriaDto);
    Categoria update(UUID id, CategoriaDto categoriaDto);
    Categoria delete(UUID id, CategoriaDto categoriaDto);
}