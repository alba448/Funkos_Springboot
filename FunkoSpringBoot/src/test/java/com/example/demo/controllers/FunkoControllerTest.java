package com.example.demo.controllers;

import com.example.demo.funko.controllers.FunkoController;
import com.example.demo.funko.dto.FunkoDto;
import com.example.demo.funko.mappers.FunkoMapper;
import com.example.demo.funko.model.Funko;
import com.example.demo.funko.service.FunkoService;
import com.example.demo.funko.service.FunkoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FunkoController.class)
public class FunkoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FunkoService funkoService;

    @MockBean
    private FunkoMapper funkoMapper;

    private Funko funko;
    private FunkoDto funkoDto;

    @BeforeEach
    void setUp() {
        funko = new Funko();
        funko.setId(1L);
        funko.setNombre("Funko Test");

        funko.setPrecio(19.99);

        funkoDto = new FunkoDto();
        funkoDto.setNombre("Funko Test");

        funkoDto.setPrecio(19.99);
    }

    @Test
    void getAll() throws Exception {
        when(funkoService.getAll()).thenReturn(Collections.singletonList(funko));

        mockMvc.perform(get("/funkos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Funko Test"));

        verify(funkoService, times(1)).getAll();
    }

    @Test
    void getById() throws Exception {
        when(funkoService.getById(anyLong())).thenReturn(funko);

        mockMvc.perform(get("/funkos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Funko Test"));

        verify(funkoService, times(1)).getById(anyLong());
    }

    @Test
    void create() throws Exception {
        when(funkoService.create(any(Funko.class))).thenReturn(funko);
        when(funkoMapper.toFunko(any(FunkoDto.class))).thenReturn(funko);

        mockMvc.perform(post("/funkos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(funkoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Funko Test"));

        verify(funkoService, times(1)).create(any(Funko.class));
        verify(funkoMapper, times(1)).toFunko(any(FunkoDto.class));
    }

    @Test
    void update() throws Exception {
        when(funkoService.update(anyLong(), any(Funko.class))).thenReturn(funko);
        when(funkoMapper.fromDto(any(FunkoDto.class))).thenReturn(funko);

        mockMvc.perform(put("/funkos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(funkoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Funko Test"));

        verify(funkoService, times(1)).update(anyLong(), any(Funko.class));
        verify(funkoMapper, times(1)).fromDto(any(FunkoDto.class));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(funkoService).delete(anyLong());

        mockMvc.perform(delete("/funkos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(funkoService, times(1)).delete(anyLong());
    }
}
