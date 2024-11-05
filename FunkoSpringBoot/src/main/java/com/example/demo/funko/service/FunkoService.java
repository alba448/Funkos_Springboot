package com.example.demo.funko.service;

import com.example.demo.funko.model.Funko;

import java.util.List;

public interface FunkoService {
    List<Funko> getAll();

    Funko getById(Long id);

    Funko create(Funko funko);

    Funko update(Long id, Funko funko);

    Funko delete(Long id);

    List<Funko> getAllByName(String nombre);
}
