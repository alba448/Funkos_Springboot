package example.demo.categoria.validators;

import example.demo.categoria.repository.CategoriaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class CategoriaValidator {
    public boolean isIdValid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}