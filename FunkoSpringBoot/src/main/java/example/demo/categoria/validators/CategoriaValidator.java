package example.demo.categoria.validators;

import example.demo.categoria.repository.CategoriaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class CategoriaValidator {
    private final CategoriaRepository categoriaRepository;

    public CategoriaValidator(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public boolean isNameUnique(String nombre) {
        return categoriaRepository.findByNombre(nombre).isEmpty();
    }
}