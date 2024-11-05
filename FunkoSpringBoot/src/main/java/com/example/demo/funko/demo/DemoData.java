package com.example.demo.funko.demo;

import com.example.demo.funko.model.Funko;

import java.util.List;

import static com.example.demo.categoria.model.CategoriaTipo.SERIE;

public class DemoData {
    public static final List<Funko> FUNKO_DEMO = List.of(
            Funko.builder().id(1L).nombre("Jose").categoriaTipo(SERIE).build(),
            Funko.builder().id(2L).nombre("Maria").categoria().build(),
            Funko.builder().id(3L).nombre("Ana").categoria().build(),
            Funko.builder().id(4L).nombre("Luis").categoria().build(),
            Funko.builder().id(5L).nombre("Pedro").categoria().build()
    );
}
