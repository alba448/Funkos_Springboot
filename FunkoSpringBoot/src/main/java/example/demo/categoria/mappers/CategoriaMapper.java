package example.demo.categoria.mappers;

import example.demo.categoria.dto.CategoriaDto;
import example.demo.categoria.model.Categoria;
import example.demo.categoria.model.TipoCategoria;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


@Component
public class CategoriaMapper {
    public Categoria toCategoria(CategoriaDto categoriaDto) {
        var categoria = new Categoria();
        categoria.setNombre(TipoCategoria.valueOf(categoriaDto.getNombre()));
        return categoria;
    }

    public Categoria toCategoriaUpdate(CategoriaDto categoriaDto, Categoria categoria){
        return new Categoria(
                categoria.getId(),
                categoriaDto.getNombre() != null ? TipoCategoria.valueOf(categoriaDto.getNombre()) : categoria.getNombre(),
                categoria.getCreatedAt(),
                LocalDateTime.now(),
                categoriaDto.getActivado() != null ? categoriaDto.getActivado() : categoria.getActivado()
        );
    }
}