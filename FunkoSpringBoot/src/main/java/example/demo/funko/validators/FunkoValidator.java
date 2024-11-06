package example.demo.funko.validators;


import example.demo.funko.repository.FunkoRepository;
import org.springframework.stereotype.Component;

@Component
public class FunkoValidator {

    private final FunkoRepository funkoRepository; // Inyecta el repositorio de Funkos

    public FunkoValidator(FunkoRepository funkoRepository) {
        this.funkoRepository = funkoRepository;
    }

    public boolean isNameUnique(String nombre) {
        return funkoRepository.findByNombre(nombre).isEmpty();
    }
}