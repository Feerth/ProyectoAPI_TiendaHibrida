package com.tiendahibrida.TIENDA_HIBRIDA.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa una categoría de productos dentro del sistema.
 * Por ejemplo: "Electrónica", "Libros", "Software".
 *
 * <p>Características:</p>
 * <ul>
 *     <li>Posee un nombre único obligatorio.</li>
 *     <li>Se relaciona con múltiples productos (OneToMany).</li>
 *     <li>Eliminación en cascada de productos asociados.</li>
 * </ul>
 *
 * Esta relación es bidireccional, donde Producto contiene el lado propietario.
 */
@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "productos")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre único de la categoría.
     * No debe ser nulo y está limitado a 100 caracteres.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    /**
     * Descripción opcional de la categoría.
     */
    @Column(length = 500)
    private String descripcion;

    /**
     * Relación OneToMany con productos.
     * Se carga de manera LAZY para optimización.
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Producto> productos = new HashSet<>();
}
