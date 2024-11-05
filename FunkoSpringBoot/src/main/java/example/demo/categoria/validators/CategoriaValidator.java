package example.demo.categoria.validators;

import example.demo.categoria.model.TipoCategoria;

import org.springframework.stereotype.Component;




@Component
public class CategoriaValidator {
    public boolean isNombreCategoriaValido(String nombre) {
        try {
            TipoCategoria.valueOf(nombre);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}