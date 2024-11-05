package com.example.demo.categoria.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.categoria.model.Categoria;
import com.example.demo.categoria.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(UUID.randomUUID());
        categoria.setTipo("Tipo Test");
    }

    @Test
    void getAll() {
        when(categoriaRepository.findAll()).thenReturn(Collections.singletonList(categoria));

        List<Categoria> result = categoriaService.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void getById() {
        when(categoriaRepository.findById(any(UUID.class))).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.getById(UUID.randomUUID());
        assertNotNull(result);

        verify(categoriaRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void create() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria result = categoriaService.create(categoria);
        assertNotNull(result);

        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void update() {
        Categoria updatedCategoria = new Categoria();
        updatedCategoria.setId(UUID.randomUUID());

        updatedCategoria.setTipo("Updated Tipo");

        when(categoriaRepository.findById(any(UUID.class))).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(updatedCategoria);

        Categoria result = categoriaService.update(UUID.randomUUID(), updatedCategoria);
        assertNotNull(result);

        verify(categoriaRepository, times(1)).findById(any(UUID.class));
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void delete() {
        when(categoriaRepository.findByIdAndActivaTrue(any(UUID.class))).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.delete(UUID.randomUUID());
        assertNotNull(result);
        verify(categoriaRepository, times(1)).findByIdAndActivaTrue(any(UUID.class));
        verify(categoriaRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void getByTipo() {
        when(categoriaRepository.findByTipo(anyString())).thenReturn(Collections.singletonList(categoria));

        List<Categoria> result = categoriaService.getByTipo("Tipo Test");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(categoriaRepository, times(1)).findByTipo(anyString());
    }
}
