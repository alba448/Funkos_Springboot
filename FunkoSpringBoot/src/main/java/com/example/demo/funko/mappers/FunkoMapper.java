package com.example.demo.funko.mappers;

import com.example.demo.categoria.model.Categoria;
import com.example.demo.funko.dto.FunkoDto;
import com.example.demo.funko.model.Funko;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class FunkoMapper {
    public Funko toFunko(FunkoDto funkoDto, Categoria categoria) {
        return new Funko(
                null,
                funkoDto.getNombre(),
                funkoDto.getPrecio(),
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public Funko toFunkoUpdate(FunkoDto funkoDto, Funko funko, Categoria categoria){
        return new Funko(
                funko.getId(),
                funkoDto.getNombre(),
                funkoDto.getPrecio(),
                categoria,
                funko.getCreatedAt(),
                LocalDateTime.now()
                );
    }
}