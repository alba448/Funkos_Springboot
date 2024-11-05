package com.example.demo.funko.model;

import com.example.demo.categoria.model.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Builder
@Data
@Entity
@Table(name = "funkos")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Funko {

    private static final Long DEFAULT_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id=DEFAULT_ID;

    @Column(name="nombre",nullable = false)
    @NotEmpty
    private String nombre;

    @Column(name="precio",nullable = false)
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;

    @ManyToOne
    @JoinColumn(name="categorias_tipo")

    private Categoria categoria;

    @Column(name="created_at")
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();
}