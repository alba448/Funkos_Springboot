package com.example.demo.categoria.controllers;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.example.demo.categoria.dto.CategoriaDto;
import com.example.demo.categoria.mappers.CategoriaMapper;
import com.example.demo.categoria.model.Categoria;
import com.example.demo.categoria.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private CategoriaMapper categoriaMapper;

    private Categoria categoria;
    private CategoriaDto categoriaDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoriaController(categoriaService, categoriaMapper)).build();

        categoria = new Categoria();
        categoria.setId(UUID.randomUUID());

        categoria.setTipo("Tipo Test");

        categoriaDto = new CategoriaDto();

        categoriaDto.setTipo("Tipo Test");
    }

    @Test
    void getAll() throws Exception {
        when(categoriaService.getAll()).thenReturn(Collections.singletonList(categoria));

        mockMvc.perform(get("/funkos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Categoria Test"));

        verify(categoriaService, times(1)).getAll();
    }

    @Test
    void getById() throws Exception {
        when(categoriaService.getById(any(UUID.class))).thenReturn(categoria);

        mockMvc.perform(get("/funkos/{id}", categoria.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Categoria Test"));

        verify(categoriaService, times(1)).getById(any(UUID.class));
    }

    @Test
    void create() throws Exception {
        when(categoriaService.create(any(Categoria.class))).thenReturn(categoria);
        when(categoriaMapper.fromDto(any(CategoriaDto.class))).thenReturn(categoria);

        mockMvc.perform(post("/funkos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoriaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Categoria Test"));

        verify(categoriaService, times(1)).create(any(Categoria.class));
        verify(categoriaMapper, times(1)).fromDto(any(CategoriaDto.class));
    }

    @Test
    void update() throws Exception {
        when(categoriaService.update(any(UUID.class), any(Categoria.class))).thenReturn(categoria);
        when(categoriaMapper.fromDto(any(CategoriaDto.class))).thenReturn(categoria);

        mockMvc.perform(put("/funkos/{id}", categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoriaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Categoria Test"));

        verify(categoriaService, times(1)).update(any(UUID.class), any(Categoria.class));
        verify(categoriaMapper, times(1)).fromDto(any(CategoriaDto.class));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(categoriaService).delete(any(UUID.class));

        mockMvc.perform(delete("/funkos/{id}", categoria.getId()))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).delete(any(UUID.class));
    }
}
