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
public class CategoriaValidatorTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaValidator categoriaValidator;

    @BeforeEach
    void setUp() {
        categoriaValidator = new CategoriaValidator(categoriaRepository);
    }

    @Test
    void testIsNameUnique_WhenNameIsUnique() {
        String nombre = "UniqueCategoria";
        when(categoriaRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        boolean result = categoriaValidator.isNameUnique(nombre);

        assertTrue(result, "El nombre no es único.");
        verify(categoriaRepository, times(1)).findByNombre(nombre);
    }

    @Test
    void testIsNameUnique_WhenNameIsNotUnique() {
        String nombre = "ExistingCategoria";
        when(categoriaRepository.findByNombre(nombre)).thenReturn(Optional.of(new Categoria()));

        boolean result = categoriaValidator.isNameUnique(nombre);

        assertFalse(result, "El nombre no es único.");
        verify(categoriaRepository, times(1)).findByNombre(nombre);
    }
}
