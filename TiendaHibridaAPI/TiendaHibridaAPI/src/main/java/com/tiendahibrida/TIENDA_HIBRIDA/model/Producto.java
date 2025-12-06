package com.tiendahibrida.TIENDA_HIBRIDA.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Clase abstracta base para todos los productos comercializados en la tienda.
 *
 * <p>Usa estrategia JOINED para herencia:</p>
 * Cada subclase almacena sus atributos específicos en una tabla propia.
 *
 * <p>Atributos comunes:</p>
 * <ul>
 *     <li>Nombre</li>
 *     <li>Precio base</li>
 *     <li>Categoría asociada</li>
 * </ul>
 *
 * Define dos métodos abstractos:
 * <ul>
 *     <li>{@link #calcularPrecioFinal()}</li>
 *     <li>{@link #obtenerDetalles()}</li>
 * </ul>
 */
@Entity
@Table(name = "producto")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = "categoria")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductoFisico.class, name = "fisico"),
        @JsonSubTypes.Type(value = ProductoDigital.class, name = "digital")
})
public abstract class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del producto.
     */
    @Column(nullable = false, length = 200)
    private String nombre;

    /**
     * Precio base antes de impuestos o recargos.
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal precio;

    /**
     * Categoría a la que pertenece el producto.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference
    private Categoria categoria;

    /** Escala estándar para operaciones monetarias. */
    protected static final int MONETARY_SCALE = 2;

    /** Modo de redondeo estándar. */
    protected static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Constructor auxiliar.
     */
    public Producto(String nombre, BigDecimal precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    /**
     * Calcula el precio final aplicando impuestos, descuentos o recargos.
     */
    public abstract BigDecimal calcularPrecioFinal();

    /**
     * Devuelve una descripción extendida del producto.
     */
    public abstract String obtenerDetalles();
}
