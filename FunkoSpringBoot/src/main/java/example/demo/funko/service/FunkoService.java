package example.demo.funko.service;

import example.demo.funko.dto.FunkoDto;
import example.demo.funko.model.Funko;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FunkoService {
    List<Funko> getAll();
    Funko getById(String id);
    Funko getByNombre(String nombre);
    Funko save(FunkoDto funkoDto);
    Funko update(String id, FunkoDto funkoDto);
    Funko delete(String id);
}