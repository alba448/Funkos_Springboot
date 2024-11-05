package com.example.demo.categoria.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
@Table(name = "categorias")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql ="UPDATE categorias SET activada=false WHERE id=?")
public class Categoria {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo",nullable = false)
    private CategoriaTipo tipo = CategoriaTipo.DISNEY;


    @Column(name="created_at")
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="update_at")
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name="activa",nullable = false)
    private Boolean activa = true;


}
