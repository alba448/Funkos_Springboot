package example.demo.funko.mappers;


import example.demo.categoria.model.Categoria;
import example.demo.funko.dto.FunkoDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FunkoMapperTest {

    private final Categoria categoria = new Categoria(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"), "DISNEY", LocalDateTime.now(), LocalDateTime.now(), true);
    private final FunkoMapper mapper = new FunkoMapper();

    @Test
    void toFunko() {
        FunkoDto funkoDto = new FunkoDto();
        funkoDto.setNombre("Darth Vader");
        funkoDto.setPrecio(10.99);
        funkoDto.setCategoria(categoria.getNombre());

        var res = mapper.toFunko(funkoDto, categoria);

        assertAll(
                () -> assertEquals(funkoDto.getNombre(), res.getNombre()),
                () -> assertEquals(funkoDto.getPrecio(), res.getPrecio()),
                () -> assertEquals(funkoDto.getCategoria(), res.getCategoria().getNombre())
        );
    }
}