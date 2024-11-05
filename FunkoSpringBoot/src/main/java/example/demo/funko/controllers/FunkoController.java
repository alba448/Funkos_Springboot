package example.demo.funko.controllers;

import example.demo.funko.dto.FunkoDto;
import example.demo.funko.mappers.FunkoMapper;
import example.demo.funko.model.Funko;
import example.demo.funko.service.FunkoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/funkos")
@Validated
public class FunkoController {
    private FunkoService service;

    @Autowired
    public FunkoController(FunkoService funkoService) {
        this.service = funkoService;
    }

    @GetMapping
    public ResponseEntity<List<Funko>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Funko> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Funko> save(@Valid @RequestBody FunkoDto funkoDto) {
        var result = service.save(funkoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("{id}")
    public ResponseEntity<Funko> update(@PathVariable Long id, @Valid @RequestBody FunkoDto funkoDto) {
        var result = service.update(id, funkoDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Funko> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}