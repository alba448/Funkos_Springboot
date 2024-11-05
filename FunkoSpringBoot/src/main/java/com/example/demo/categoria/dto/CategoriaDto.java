package com.example.demo.categoria.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDto {
        Boolean activa;

        @NotBlank(message = "El tipo no puede estar vacío")
        String tipo;
}