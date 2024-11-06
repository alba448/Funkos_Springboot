package example.demo.funko.service;


import example.demo.categoria.model.Categoria;
import example.demo.categoria.service.CategoriaService;
import example.demo.funko.dto.FunkoDto;
import example.demo.funko.mappers.FunkoMapper;
import example.demo.funko.model.Funko;
import example.demo.funko.repository.FunkoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunkoServiceImplTest {
    @Mock
    private FunkoRepository repository;

    @Mock
    private FunkoMapper mapper;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private FunkoServiceImpl service;

    private Funko funkoTest;
    private Categoria categoriaTest;

    @BeforeEach
    void setUp() {
        categoriaTest = new Categoria();
        categoriaTest.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        categoriaTest.setNombre("PELICULA");
        categoriaTest.setActivado(true);

        funkoTest = new Funko();
        funkoTest.setNombre("Darth Vader");
        funkoTest.setPrecio(10.99);
        funkoTest.setCategoria(categoriaTest);
    }

    @Test
    void getAll() {
        when(service.getAll()).thenReturn(List.of(funkoTest));

        var result = service.getAll();

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.contains(funkoTest)),
                () -> assertEquals("Darth Vader", result.get(0).getNombre()),
                () -> assertEquals(10.99, result.get(0).getPrecio()),
                () -> assertEquals(categoriaTest, result.get(0).getCategoria())
        );

        verify(repository, times(1)).findAll();
    }

    @Test
    void getById() {
        when(repository.findById(1L)).thenReturn(Optional.of(funkoTest));

        var result = service.getById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Darth Vader", result.getNombre()),
                () -> assertEquals(10.99, result.getPrecio()),
                () -> assertEquals(categoriaTest, result.getCategoria())
        );

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void save() {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setId(UUID.fromString("5790bdd4-8898-4c61-b547-bc26952dc2a3"));
        nuevaCategoria.setNombre("DISNEY");
        nuevaCategoria.setActivado(true);

        FunkoDto nuevoFunko = new FunkoDto();
        nuevoFunko.setNombre("Mickey Mouse");
        nuevoFunko.setPrecio(7.95);
        nuevoFunko.setCategoria(nuevaCategoria.getNombre());

        Funko funkoMapped = new Funko();
        funkoMapped.setNombre(nuevoFunko.getNombre());
        funkoMapped.setPrecio(nuevoFunko.getPrecio());
        funkoMapped.setCategoria(nuevaCategoria);

        when(mapper.toFunko(nuevoFunko, nuevaCategoria)).thenReturn(funkoMapped);
        when(repository.save(funkoMapped)).thenReturn(funkoMapped);
        when(categoriaService.getByNombre("DISNEY")).thenReturn(nuevaCategoria);

        var result = service.save(nuevoFunko);

        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals("Mickey Mouse", result.getNombre()),
                () -> assertEquals(7.95, result.getPrecio()),
                () -> assertEquals(nuevaCategoria, result.getCategoria())
        );

        verify(repository, times(1)).save(funkoMapped);
        verify(mapper, times(1)).toFunko(nuevoFunko, nuevaCategoria);
        verify(categoriaService, times(1)).getByNombre(nuevaCategoria.getNombre());
    }

    @Test
    void update() {
        Categoria updatedCategoria = new Categoria();
        updatedCategoria.setId(UUID.fromString("5790bdd4-8898-4c61-b547-bc26952dc2a3"));
        updatedCategoria.setNombre("SUPERHEROES");
        updatedCategoria.setActivado(true);

        FunkoDto updatedFunkoDto = new FunkoDto();
        updatedFunkoDto.setNombre("Superman");
        updatedFunkoDto.setPrecio(15.99);
        updatedFunkoDto.setCategoria(updatedCategoria.getNombre());

        Funko updatedFunko = new Funko();
        updatedFunko.setId(2L);
        updatedFunko.setNombre("Superman");
        updatedFunko.setPrecio(15.99);
        updatedFunko.setCategoria(updatedCategoria);

        when(repository.findById(2L)).thenReturn(Optional.of(updatedFunko));
        when(repository.save(updatedFunko)).thenReturn(updatedFunko);
        when(categoriaService.getByNombre("SUPERHEROES")).thenReturn(updatedCategoria);

        var result = service.update(2L, updatedFunkoDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2L, result.getId()),
                () -> assertEquals("Superman", result.getNombre()),
                () -> assertEquals(15.99, result.getPrecio()),
                () -> assertEquals(updatedCategoria, result.getCategoria())
        );

        verify(repository, times(1)).findById(2L);
        verify(repository, times(1)).save(updatedFunko);
        verify(categoriaService, times(1)).getByNombre(updatedCategoria.getNombre());
    }

    @Test
    void delete() {
        when(repository.findById(1L)).thenReturn(Optional.of(funkoTest));

        var result = service.delete(1L);

        assertAll(
                () -> assertEquals("Darth Vader", result.getNombre()),
                () -> assertEquals(10.99, result.getPrecio()),
                () -> assertEquals(categoriaTest, result.getCategoria())
        );

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}