package example.demo.funko.validators;

import example.demo.funko.repository.FunkoRepository;
import org.springframework.stereotype.Component;

@Component
public class FunkoValidator {
    public boolean isIdValid(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}