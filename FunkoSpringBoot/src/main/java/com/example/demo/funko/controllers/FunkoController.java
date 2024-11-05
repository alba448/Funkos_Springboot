package com.example.demo.funko.controllers;

import com.example.demo.funko.dto.FunkoDto;
import com.example.demo.funko.mappers.FunkoMapper;
import com.example.demo.funko.model.Funko;
import com.example.demo.funko.service.FunkoService;
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


@RestController
@RequestMapping("/funkos")
public class FunkoController {

    private final Logger logger = LoggerFactory.getLogger(FunkoController.class);

    private final FunkoService funkoService;
    private final FunkoMapper funkoMapper;

    @Autowired
    public FunkoController(FunkoService funkoService, FunkoMapper funkoMapper) {
        this.funkoService = funkoService;
        this.funkoMapper = funkoMapper;
    }

    @GetMapping
    public ResponseEntity<List<Funko>> getAll(@RequestParam(required = false) String nombre) {
        logger.info("Obteniendo funkos");
        List<Funko> result;
        if (nombre != null) {
            result = funkoService.getAllByName(nombre);
        } else {
            result = funkoService.getAll();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<Funko> getById(@PathVariable Long id) {
        logger.info("Obteniendo funkos por id");
        var result = funkoService.getById(id);
        return ResponseEntity.ok(result);

    }

    @PostMapping
    public ResponseEntity<Funko> create(@Valid @RequestBody FunkoDto dto) {
        logger.info("Creando funko");
        var result = funkoService.create(funkoMapper.toFunko(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<Funko> update(@PathVariable Long id, @Valid @RequestBody FunkoDto dto) {
        logger.info("Actualizando funko");
        var result = funkoService.update(id, funkoMapper.fromDto(dto));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Funko> delete(@PathVariable Long id) {
        logger.info("Borrando funko");
        var result = funkoService.delete(id);
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