package example.demo.categoria.service;


import example.demo.categoria.dto.CategoriaDto;
import example.demo.categoria.mappers.CategoriaMapper;
import example.demo.categoria.model.Categoria;
import example.demo.categoria.repository.CategoriaRepository;
import example.demo.categoria.validators.CategoriaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {
    @Mock
    private CategoriaRepository repository;

    @Mock
    private CategoriaMapper mapper;

    @Mock
    private CategoriaValidator validator;

    @InjectMocks
    private CategoriaServiceImpl service;

    private Categoria categoriaTest;

    @BeforeEach
    void setUp() {
        categoriaTest = new Categoria();
        categoriaTest.setId(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
        categoriaTest.setNombre("DISNEY");
        categoriaTest.setActivado(true);
    }

    @Test
    void getAll() {
        when(service.getAll()).thenReturn(List.of(categoriaTest));

        var result = service.getAll();

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.contains(categoriaTest)),
                () -> assertEquals("DISNEY", result.get(0).getNombre()),
                () -> assertTrue(result.get(0).getActivado())
        );

        verify(repository, times(1)).findAll();
    }

    @Test
    void getById() {
        when(repository.findById(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"))).thenReturn(Optional.of(categoriaTest));

        var result = service.getById(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("DISNEY", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).findById(UUID.fromString("12d45756-3895-49b2-90d3-c4a12d5ee081"));
    }

    @Test
    void getByIdNotFound() {
        when(repository.findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"))).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.getById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"))
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoria con id 4182d617-ec89-4fbc-be95-85e461778700 no se ha encontrado.", thrown.getReason());

        verify(repository, times(1)).findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"));
    }

    @Test
    void getByNombre() {
        when(repository.findByNombre("DISNEY")).thenReturn(Optional.ofNullable(categoriaTest));

        var result = service.getByNombre("DISNEY");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("DISNEY", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).findByNombre("DISNEY");
    }

    @Test
    void getByNombreNotFound() {
        when(repository.findByNombre("CategoriaTestNotFound")).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.getByNombre("CategoriaTestNotFound")
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoria CategoriaTestNotFound no existe", thrown.getReason());

        verify(repository, times(1)).findByNombre("CategoriaTestNotFound");
    }

    @Test
    void save() {
        CategoriaDto nuevaCategoria = new CategoriaDto();
        nuevaCategoria.setNombre("DISNEY");
        nuevaCategoria.setActivado(true);

        Categoria categoria = new Categoria();
        categoria.setNombre(nuevaCategoria.getNombre());
        categoria.setActivado(nuevaCategoria.getActivado());

        when(validator.isNameUnique(nuevaCategoria.getNombre())).thenReturn(true);
        when(mapper.fromDto(nuevaCategoria)).thenReturn(categoria);
        when(repository.save(categoria)).thenReturn(categoria);

        var result = service.save(nuevaCategoria);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("DISNEY", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).save(categoria);
        verify(mapper, times(1)).fromDto(nuevaCategoria);
    }

    @Test
    void update() {
        UUID id = UUID.randomUUID();
        CategoriaDto updatedCategoria = new CategoriaDto();
        updatedCategoria.setId(id);
        updatedCategoria.setNombre("SUPERHEROES");
        updatedCategoria.setActivado(true);

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNombre("OLD_NAME");
        categoriaExistente.setActivado(true);

        Categoria updatedCategoriaEntity = new Categoria();
        updatedCategoriaEntity.setId(id);
        updatedCategoriaEntity.setNombre("SUPERHEROES");
        updatedCategoriaEntity.setActivado(true);

        when(repository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(mapper.toCategoria(updatedCategoria, categoriaExistente)).thenReturn(updatedCategoriaEntity);
        when(repository.save(updatedCategoriaEntity)).thenReturn(updatedCategoriaEntity);

        var result = service.update(id, updatedCategoria);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("SUPERHEROES", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(updatedCategoriaEntity);
        verify(mapper, times(1)).toCategoria(updatedCategoria, categoriaExistente);
    }

    @Test
    void updateNotFound() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700");
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.update(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"), categoriaUpdateDto)
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoria con id 4182d617-ec89-4fbc-be95-85e461778700 no se ha encontrado.", thrown.getReason());

        verify(repository, times(1)).findById(id);
    }

    @Test
    void delete() {
        UUID id = UUID.fromString("79741172-6da6-47f1-9525-a6c83053f856");
        CategoriaDto categoriaBorrada = new CategoriaDto();
        categoriaBorrada.setId(id);
        categoriaBorrada.setNombre("SERIE");
        categoriaBorrada.setActivado(true);

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNombre("SERIE");
        categoriaExistente.setActivado(true);

        Categoria updatedCategoriaEntity = new Categoria();
        updatedCategoriaEntity.setId(id);
        updatedCategoriaEntity.setNombre("SERIE");
        updatedCategoriaEntity.setActivado(true);

        when(repository.findByIdAndActivadoTrue(id)).thenReturn(Optional.of(categoriaExistente));
        when(mapper.toCategoria(categoriaBorrada, categoriaExistente)).thenReturn(updatedCategoriaEntity);
        when(repository.save(updatedCategoriaEntity)).thenReturn(updatedCategoriaEntity);

        var result = service.delete(id, categoriaBorrada);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("SERIE", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).findByIdAndActivadoTrue(id);
        verify(repository, times(1)).save(updatedCategoriaEntity);
        verify(mapper, times(1)).toCategoria(categoriaBorrada, categoriaExistente);
    }

    @Test
    void deleteNotFound() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766");
        CategoriaDto categoriaBorradaDto = new CategoriaDto();
        categoriaBorradaDto.setNombre("CategoriaTest");
        categoriaBorradaDto.setActivado(true);

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNombre("CategoriaTest");
        categoriaExistente.setActivado(true);

        Categoria categoriaBorrada = new Categoria();
        categoriaBorrada.setId(id);
        categoriaBorrada.setNombre("CategoriaTest");
        categoriaBorrada.setActivado(true);

        when(repository.findByIdAndActivadoTrue(id)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.delete(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766"), categoriaBorradaDto)
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("La categoria con id 4182d617-ec89-4fbc-be95-85e461778766 no se ha encontrado.", thrown.getReason());

        verify(repository, times(1)).findByIdAndActivadoTrue(id);
    }
}