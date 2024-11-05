package com.example.demo.service;

import com.example.demo.funko.model.Funko;
import com.example.demo.funko.repository.FunkoRepository;
import com.example.demo.funko.service.FunkoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FunkoServiceImplTest {

    @Mock
    private FunkoRepository funkoRepository;

    @InjectMocks
    private FunkoServiceImpl funkoService;

    private Funko funko;

    @BeforeEach
    void setUp() {
        funko = new Funko();
        funko.setId(1L);
        funko.setNombre("Funko Test");
        funko.setPrecio(19.99);
    }

    @Test
    void getAll() {
        List<Funko> funkos = new ArrayList<>();
        funkos.add(funko);

        when(funkoRepository.findAll()).thenReturn(funkos);

        List<Funko> result = funkoService.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(funkoRepository, times(1)).findAll();
    }

    @Test
    void getById() {
        when(funkoRepository.findById(anyLong())).thenReturn(Optional.of(funko));

        Funko result = funkoService.getById(1L);
        assertNotNull(result);
        assertEquals(funko.getNombre(), result.getNombre());
        verify(funkoRepository, times(1)).findById(anyLong());
    }

    @Test
    void create() {
        when(funkoRepository.save(any(Funko.class))).thenReturn(funko);

        Funko result = funkoService.create(funko);
        assertNotNull(result);
        assertEquals(funko.getNombre(), result.getNombre());
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    @Test
    void update() {
        Funko updatedFunko = new Funko();
        updatedFunko.setId(1L);
        updatedFunko.setNombre("Updated Funko");
        updatedFunko.setPrecio(29.99);

        when(funkoRepository.findById(anyLong())).thenReturn(Optional.of(funko));
        when(funkoRepository.save(any(Funko.class))).thenReturn(updatedFunko);

        Funko result = funkoService.update(1L, updatedFunko);
        assertNotNull(result);
        assertEquals(updatedFunko.getNombre(), result.getNombre());
        verify(funkoRepository, times(1)).findById(anyLong());
        verify(funkoRepository, times(1)).save(any(Funko.class));
    }

    @Test
    void delete() {
        when(funkoRepository.findById(anyLong())).thenReturn(Optional.of(funko));

        Funko result = funkoService.delete(1L);
        assertNotNull(result);
        verify(funkoRepository, times(1)).findById(anyLong());
        verify(funkoRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getAllByName() {
        List<Funko> funkos = new ArrayList<>();
        funkos.add(funko);

        when(funkoRepository.findByNombreContainingIgnoreCase(anyString())).thenReturn(funkos);

        List<Funko> result = funkoService.getAllByName("Funko");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(funkoRepository, times(1)).findByNombreContainingIgnoreCase(anyString());
    }
}
