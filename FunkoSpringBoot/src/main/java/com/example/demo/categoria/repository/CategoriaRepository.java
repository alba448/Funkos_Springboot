package com.example.demo.categoria.repository;

import com.example.demo.categoria.model.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    List<Categoria> findByTipo(String tipo);

    Optional<Categoria> findByIdAndActivaTrue(UUID id);



}