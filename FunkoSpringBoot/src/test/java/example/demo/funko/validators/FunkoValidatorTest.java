package example.demo.funko.validators;

import example.demo.funko.model.Funko;
import example.demo.funko.repository.FunkoRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FunkoValidatorTest {

    @Mock
    private FunkoRepository funkoRepository;

    @InjectMocks
    private FunkoValidator funkoValidator;

    @BeforeEach
    void setUp() {
        funkoValidator = new FunkoValidator(funkoRepository);
    }

    @Test
    void testIsNameUnique_WhenNameIsUnique() {
        String nombre = "UniqueFunko";
        when(funkoRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        boolean result = funkoValidator.isNameUnique(nombre);

        assertTrue(result, "Expected the name to be unique, but it was not.");
        verify(funkoRepository, times(1)).findByNombre(nombre);
    }

    @Test
    void testIsNameUnique_WhenNameIsNotUnique() {
        String nombre = "ExistingFunko";
        when(funkoRepository.findByNombre(nombre)).thenReturn(Optional.of(new Funko()));

        boolean result = funkoValidator.isNameUnique(nombre);

        assertFalse(result, "Expected the name not to be unique, but it was.");
        verify(funkoRepository, times(1)).findByNombre(nombre);
    }
}
