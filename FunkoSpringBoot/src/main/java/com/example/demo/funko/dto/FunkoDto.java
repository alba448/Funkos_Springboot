package com.example.demo.funko.dto;


import com.example.demo.categoria.model.CategoriaTipo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FunkoDto {
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre;

        @Min(value = 0, message = "El precio no puede ser negativo")
        double precio;

        @NotNull(message = "El tipo no puede estar vacío")
        CategoriaTipo categoria;
}