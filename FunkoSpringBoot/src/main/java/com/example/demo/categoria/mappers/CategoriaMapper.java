package com.example.demo.categoria.mappers;

import com.example.demo.categoria.dto.CategoriaDto;
import com.example.demo.categoria.model.Categoria;
import com.example.demo.categoria.model.CategoriaTipo;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class CategoriaMapper {
    public Categoria fromDto(CategoriaDto categoriaDto) {
        var categoria = new Categoria();
        categoria.setTipo(CategoriaTipo.valueOf(categoriaDto.getTipo()));
        return categoria;
    }

    public Categoria toCategoria(CategoriaDto categoriaDto, Categoria categoria){
        return new Categoria(
                categoria.getId(),
                categoriaDto.getTipo() != null ? CategoriaTipo.valueOf(categoriaDto.getTipo()) : categoria.getTipo(),
                categoria.getCreatedAt(),
                LocalDateTime.now(),
                categoriaDto.getActiva() != null ? categoriaDto.getActiva() : categoria.getActiva()
                );
    }
}