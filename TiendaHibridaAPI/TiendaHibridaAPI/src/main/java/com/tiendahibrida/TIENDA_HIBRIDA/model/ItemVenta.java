package com.tiendahibrida.TIENDA_HIBRIDA.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Entidad que representa un ítem dentro de una Venta.
 * Contiene el producto vendido, la cantidad adquirida, y el precio unitario
 * congelado al momento de la operación.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *     <li>Almacenar precio unitario inmutable al momento de la venta.</li>
 *     <li>Calcular subtotal.</li>
 *     <li>Mantener relación bidireccional con Venta.</li>
 * </ul>
 */
@Entity
@Table(name = "item_venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"producto", "venta"})
public class ItemVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cantidad de producto vendida.
     */
    @Column(nullable = false)
    private int cantidad;

    /**
     * Precio unitario fijado al momento de la venta.
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal precioUnitario;

    /**
     * Producto asociado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /**
     * Venta a la que pertenece este ítem.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonBackReference
    private Venta venta;

    /**
     * Constructor que fija el precio al momento de crearse el ítem.
     */
    public ItemVenta(Producto producto, int cantidad) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser nulo.");
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");

        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.calcularPrecioFinal();
    }

    /**
     * Calcula el subtotal del ítem.
     */
    public BigDecimal getSubtotal() {
        return precioUnitario
                .multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
