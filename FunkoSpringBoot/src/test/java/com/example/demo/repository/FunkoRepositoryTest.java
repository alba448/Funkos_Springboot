package com.example.demo.repository;

import com.example.demo.funko.model.Funko;
import com.example.demo.funko.repository.FunkoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FunkoRepositoryTest {

    @Autowired
    private FunkoRepository funkoRepository;

    @Test
    void testFindByNombreContainingIgnoreCase() {
        Funko funko = new Funko();
        funko.setNombre("Funko Test");

        funko.setPrecio(19.99);
        funkoRepository.save(funko);

        List<Funko> found = funkoRepository.findByNombreContainingIgnoreCase("funko");
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals("Funko Test", found.get(0).getNombre());
    }

    @Test
    void testFindByCategoria() {
        Funko funko1 = new Funko();
        funko1.setNombre("Funko Pop 1");
        /*funko1.setCategoria("Pop");*/

        funko1.setPrecio(10.99);

        Funko funko2 = new Funko();
        funko2.setNombre("Funko Pop 2");
        /*funko2.setCategoria("Pop");*/

        funko2.setPrecio(15.99);

        funkoRepository.save(funko1);
        funkoRepository.save(funko2);

        List<Funko> found = funkoRepository.findByCategoria("Pop");
        assertFalse(found.isEmpty());
        assertEquals(2, found.size());
        assertEquals("Funko Pop 1", found.get(0).getNombre());
        assertEquals("Funko Pop 2", found.get(1).getNombre());
    }
}
