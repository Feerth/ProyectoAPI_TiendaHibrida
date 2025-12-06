package com.tiendahibrida.TIENDA_HIBRIDA.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que almacena la información de dirección de un cliente.
 * Es el lado NO propietario de la relación OneToOne con Cliente.
 */
@Entity
@Table(name = "direccion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Calle y número del domicilio.
     */
    @Column(nullable = false, length = 200)
    private String calle;

    /**
     * Ciudad de la dirección.
     */
    @Column(nullable = false, length = 100)
    private String ciudad;

    /**
     * Código postal.
     */
    @Column(nullable = false, length = 20)
    private String codigoPostal;

    /**
     * Relación bidireccional con Cliente.
     */
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "direccion")
    @JsonBackReference
    private Cliente cliente;
}
