package example.demo.funko.repository;

import example.demo.funko.model.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FunkoRepository extends JpaRepository<Funko, Long> {
    Optional<Funko> findById(Long id);
    Funko save(Funko funko);
}