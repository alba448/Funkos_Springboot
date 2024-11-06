package example.demo.categoria.validators;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import example.demo.categoria.model.Categoria;
import example.demo.categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoriaValidatorTest {
    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaValidator categoriaValidator;

    @Test
    void isNameUnique() {
        String nombre = "CategoriaNueva";

        when(repository.findByNombre(nombre)).thenReturn(Optional.empty());

        boolean result = categoriaValidator.isNameUnique(nombre);

        assertTrue(result);

        verify(repository, times(1)).findByNombre(nombre);
    }

    @Test
    void isNameUniqueFalse() {
        String nombre = "CategoriaExistente";

        when(repository.findByNombre(nombre)).thenReturn(Optional.of(new Categoria()));

        boolean result = categoriaValidator.isNameUnique(nombre);

        assertFalse(result);

        verify(repository, times(1)).findByNombre(nombre);
    }

    @Test
    void isIdValid() {
        String idValida = "4182d617-ec89-4fbc-be95-85e461778766";

        boolean result = categoriaValidator.isIdValid(idValida);

        assertTrue(result);
    }

    @Test
    void isIdInvalid() {
        String idInvalida = "4182d617-ec89-4f";

        boolean result = categoriaValidator.isIdValid(idInvalida);

        assertFalse(result);
    }
}