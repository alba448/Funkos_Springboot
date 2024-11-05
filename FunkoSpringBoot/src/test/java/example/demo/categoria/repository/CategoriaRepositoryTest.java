package com.example.demo.categoria.repository;

import static org.junit.jupiter.api.Assertions.*;



import com.example.demo.categoria.model.Categoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testFindByTipo() {
        Categoria categoria = new Categoria();
        categoria.setId(UUID.randomUUID());

        categoria.setTipo("Tipo Test");
        categoria.setActiva(true);
        categoriaRepository.save(categoria);

        List<Categoria> found = categoriaRepository.findByTipo("Tipo Test");
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());

    }

    @Test
    void testFindByIdAndActivaTrue() {
        UUID id = UUID.randomUUID();
        Categoria categoria = new Categoria();
        categoria.setId(id);

        categoria.setTipo("Tipo Test");
        categoria.setActiva(true);
        categoriaRepository.save(categoria);

        Optional<Categoria> found = categoriaRepository.findByIdAndActivaTrue(id);
        assertTrue(found.isPresent());

    }
}
