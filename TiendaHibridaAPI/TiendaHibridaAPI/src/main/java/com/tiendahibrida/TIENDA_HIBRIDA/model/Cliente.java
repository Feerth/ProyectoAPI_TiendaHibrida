package com.tiendahibrida.TIENDA_HIBRIDA.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un cliente registrado en la tienda.
 * Contiene información personal básica y su dirección principal.
 *
 * <p>Relaciones:</p>
 * <ul>
 *     <li>OneToOne con {@link Direccion}, obligatorio.</li>
 *     <li>OneToMany con {@link Venta}, que representa su historial.</li>
 * </ul>
 *
 * La eliminación del cliente elimina también su dirección e historial.
 */
@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"direccion", "ventas"})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del cliente.
     */
    @Column(nullable = false, length = 150)
    private String nombre;

    /**
     * Correo electrónico único del cliente.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Dirección asociada.
     * Es obligatoria y su ciclo de vida depende del cliente.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "direccion_id", nullable = false)
    @JsonIgnoreProperties("cliente")
    private Direccion direccion;

    /**
     * Historial de ventas realizadas por el cliente.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Venta> ventas = new HashSet<>();
}
