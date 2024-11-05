package example.demo.funko.repository;

import example.demo.funko.model.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface FunkoRepository extends JpaRepository<Funko, Long> {

    List<Funko> findByNombreContainingIgnoreCase(String nombre);
    List<Funko> findByCategoria(String categoria);

}