package example.demo.funko.mappers;

import example.demo.categoria.model.Categoria;
import example.demo.funko.dto.FunkoDto;
import example.demo.funko.model.Funko;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class FunkoMapper {
    public Funko toFunko(FunkoDto funkoDto, Categoria categoria) {
        return new Funko(
                null,
                funkoDto.getNombre(),
                funkoDto.getPrecio(),
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}