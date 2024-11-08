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
        categoriaTest.setId(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766"));
        categoriaTest.setNombre("CategoriaTest");
        categoriaTest.setActivado(true);
    }

    @Test
    void getAll() {
        when(service.getAll()).thenReturn(List.of(categoriaTest));

        var result = service.getAll();

        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertTrue(result.contains(categoriaTest)),
                () -> assertEquals("CategoriaTest", result.getFirst().getNombre()),
                () -> assertTrue(result.getFirst().getActivado())
        );

        verify(repository, times(1)).findAll();
    }

    @Test
    void getById() {
        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778766")).thenReturn(true);
        when(repository.findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766"))).thenReturn(Optional.of(categoriaTest));

        var result = service.getById("4182d617-ec89-4fbc-be95-85e461778766");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("CategoriaTest", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778766");
        verify(repository, times(1)).findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766"));
    }

    @Test
    void getByIdNotFound() {
        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778700")).thenReturn(true);
        when(repository.findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"))).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.getById("4182d617-ec89-4fbc-be95-85e461778700")
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("No existe la categoria con id 4182d617-ec89-4fbc-be95-85e461778700", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778700");
        verify(repository, times(1)).findById(UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700"));
    }

    @Test
    void getByIdNotValid() {
        when(validator.isIdValid("4182d617-ec89-4f")).thenReturn(false);

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.getById("4182d617-ec89-4f")
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("La id no es válida. Debe ser un UUID", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4f");
    }

    @Test
    void getByNombre() {
        when(repository.findByNombre("CategoriaTest")).thenReturn(Optional.ofNullable(categoriaTest));

        var result = service.getByNombre("CategoriaTest");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("CategoriaTest", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).findByNombre("CategoriaTest");
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
        CategoriaDto nuevaCategoriaDto = new CategoriaDto();
        nuevaCategoriaDto.setNombre("CategoriaTest");
        nuevaCategoriaDto.setActivado(true);

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("CategoriaTest");
        nuevaCategoria.setActivado(true);

        when(repository.findByNombre(nuevaCategoriaDto.getNombre())).thenReturn(Optional.empty());
        when(mapper.toCategoria(nuevaCategoriaDto)).thenReturn(nuevaCategoria);
        when(repository.save(nuevaCategoria)).thenReturn(nuevaCategoria);

        var result = service.save(nuevaCategoriaDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("CategoriaTest", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(repository, times(1)).findByNombre(nuevaCategoriaDto.getNombre());
        verify(repository, times(1)).save(nuevaCategoria);
        verify(mapper, times(1)).toCategoria(nuevaCategoriaDto);
    }

    @Test
    void saveInvalidName() {
        CategoriaDto nuevaCategoriaDto = new CategoriaDto();
        nuevaCategoriaDto.setNombre("CategoriaTest");
        nuevaCategoriaDto.setActivado(true);

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("CategoriaTest");
        nuevaCategoria.setActivado(true);

        when(repository.findByNombre(nuevaCategoriaDto.getNombre())).thenReturn(Optional.of(nuevaCategoria));

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.save(nuevaCategoriaDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("El nombre de la categoria ya existe", thrown.getReason());

        verify(repository, times(1)).findByNombre(nuevaCategoriaDto.getNombre());
    }

    @Test
    void update() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766");
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNombre("CategoriaTest");
        categoriaExistente.setActivado(true);

        Categoria categoriaUpdate = new Categoria();
        categoriaUpdate.setId(id);
        categoriaUpdate.setNombre("CategoriaTestUpdate");
        categoriaUpdate.setActivado(true);

        Categoria categoriaMocked = new Categoria();
        categoriaMocked.setNombre("CategoriaTestUpdate");

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778766")).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(repository.findByNombre(categoriaUpdateDto.getNombre())).thenReturn(Optional.empty());
        when(mapper.toCategoriaUpdate(categoriaUpdateDto, categoriaExistente)).thenReturn(categoriaUpdate);
        when(repository.save(categoriaUpdate)).thenReturn(categoriaUpdate);

        var result = service.update(id.toString(), categoriaUpdateDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("CategoriaTestUpdate", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778766");
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(categoriaUpdate);
        verify(repository, times(1)).findByNombre(categoriaUpdateDto.getNombre());
    }

    @Test
    void updateNotValidId() {
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4f")).thenReturn(false);

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.update("4182d617-ec89-4f", categoriaUpdateDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("La id no es válida. Debe ser un UUID", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4f");
    }

    @Test
    void updateNotFound() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778700");
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778700")).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.update("4182d617-ec89-4fbc-be95-85e461778700", categoriaUpdateDto)
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("No existe la categoria con id 4182d617-ec89-4fbc-be95-85e461778700", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778700");
        verify(repository, times(1)).findById(id);
    }

    @Test
    void updateNotValidName() {
        UUID id = UUID.fromString("4182d617-ec89-4fbc-be95-85e461778766");
        CategoriaDto categoriaUpdateDto = new CategoriaDto();
        categoriaUpdateDto.setNombre("CategoriaTestUpdate");
        categoriaUpdateDto.setActivado(true);

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setNombre("CategoriaTest");
        categoriaExistente.setActivado(true);

        Categoria categoriaMocked = new Categoria();
        categoriaMocked.setNombre("CategoriaTestUpdate");
        categoriaMocked.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778766")).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(repository.findByNombre(categoriaUpdateDto.getNombre())).thenReturn(Optional.of(categoriaMocked));

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.update("4182d617-ec89-4fbc-be95-85e461778766", categoriaUpdateDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("El nombre de la categoria ya existe", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778766");
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).findByNombre(categoriaUpdateDto.getNombre());
    }

    @Test
    void delete() {
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

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778766")).thenReturn(true);
        when(repository.findByIdAndActivadoTrue(id)).thenReturn(Optional.of(categoriaExistente));
        when(mapper.toCategoriaUpdate(categoriaBorradaDto, categoriaExistente)).thenReturn(categoriaBorrada);
        when(repository.save(categoriaBorrada)).thenReturn(categoriaBorrada);

        var result = service.delete(id.toString(), categoriaBorradaDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("CategoriaTest", result.getNombre()),
                () -> assertTrue(result.getActivado())
        );

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778766");
        verify(repository, times(1)).findByIdAndActivadoTrue(id);
        verify(repository, times(1)).save(categoriaBorrada);
        verify(mapper, times(1)).toCategoriaUpdate(categoriaBorradaDto, categoriaExistente);
    }

    @Test
    void deleteNotValidId() {
        CategoriaDto categoriaBorradaDto = new CategoriaDto();
        categoriaBorradaDto.setNombre("CategoriaTest");
        categoriaBorradaDto.setActivado(true);

        when(validator.isIdValid("4182d617-ec89-4f")).thenReturn(false);

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.delete("4182d617-ec89-4f", categoriaBorradaDto)
        );

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("La id no es válida. Debe ser un UUID", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4f");
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

        when(validator.isIdValid("4182d617-ec89-4fbc-be95-85e461778766")).thenReturn(true);
        when(repository.findByIdAndActivadoTrue(id)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(
                ResponseStatusException.class, () -> service.delete("4182d617-ec89-4fbc-be95-85e461778766", categoriaBorradaDto)
        );

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("No existe la categoria con id 4182d617-ec89-4fbc-be95-85e461778766", thrown.getReason());

        verify(validator, times(1)).isIdValid("4182d617-ec89-4fbc-be95-85e461778766");
        verify(repository, times(1)).findByIdAndActivadoTrue(id);
    }
}