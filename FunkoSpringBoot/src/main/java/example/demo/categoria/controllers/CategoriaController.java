package example.demo.categoria.controllers;


import example.demo.categoria.dto.CategoriaDto;
import example.demo.categoria.model.Categoria;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import example.demo.categoria.service.CategoriaServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private CategoriaServiceImpl service;

    @Autowired
    public CategoriaController(CategoriaServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Categoria> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> save(@Valid @RequestBody CategoriaDto categoriaDto) {
        var result = service.save(categoriaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<Categoria> update(@PathVariable String id, @RequestBody CategoriaDto categoriaDto) {
        var result = service.update(id, categoriaDto);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Categoria> delete(@PathVariable String id, @RequestBody CategoriaDto categoriaDto) {
        var result = service.delete(id, categoriaDto);
        return ResponseEntity.ok(result);
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