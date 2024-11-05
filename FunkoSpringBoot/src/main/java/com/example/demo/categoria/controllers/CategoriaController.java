package com.example.demo.categoria.controllers;

import com.example.demo.categoria.dto.CategoriaDto;
import com.example.demo.categoria.mappers.CategoriaMapper;
import com.example.demo.categoria.model.Categoria;
import com.example.demo.categoria.service.CategoriaService;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/funkos")
public class CategoriaController {

    private final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll(@RequestParam(required = false) String tipo) {
        logger.info("Obteniendo categorias");
        List<Categoria> result;
        if (tipo != null) {
            result = categoriaService.getByTipo(tipo);
        } else {
            result = categoriaService.getAll();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<Categoria> getById(@PathVariable UUID id) {
        logger.info("Obteniendo categoria por id");
        var result = categoriaService.getById(id);
        return ResponseEntity.ok(result);

    }

    @PostMapping
    public ResponseEntity<Categoria> create(@Valid @RequestBody CategoriaDto dto) {
        logger.info("Creando categoria");
        var result = categoriaService.create(categoriaMapper.fromDto(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<Categoria> update(@PathVariable UUID id, @Valid @RequestBody CategoriaDto dto) {
        logger.info("Actualizando categoria");
        var result = categoriaService.update(id, categoriaMapper.fromDto(dto));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Categoria> delete(@PathVariable UUID id) {
        logger.info("Borrando categoria");
        var result = categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}