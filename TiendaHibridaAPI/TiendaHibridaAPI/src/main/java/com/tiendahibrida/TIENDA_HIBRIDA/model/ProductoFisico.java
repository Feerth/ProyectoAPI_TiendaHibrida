package com.tiendahibrida.TIENDA_HIBRIDA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa un producto físico almacenado en inventario.
 *
 * <p>Atributos adicionales:</p>
 * <ul>
 *     <li>Stock disponible</li>
 *     <li>Peso en kilogramos</li>
 *     <li>Dimensiones físicas</li>
 * </ul>
 *
 * <p>Regla de precio:</p>
 * Si el peso supera 1 kg se agrega un recargo del 10%.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ProductoFisico extends Producto {

    private int stock;

    @Column(precision = 10, scale = 4)
    private BigDecimal pesoKg;

    @Column(length = 100)
    private String dimensiones;

    /**
     * Reduce el stock validando la cantidad y disponibilidad.
     */
    public void reducirStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reducir debe ser mayor que 0.");
        }
        if (cantidad > stock) {
            throw new IllegalStateException("Stock insuficiente. Disponible: "
                    + stock + ", Solicitado: " + cantidad);
        }
        this.stock -= cantidad;
    }

    /**
     * Calcula el precio final aplicando recargo por peso.
     */
    @Override
    public BigDecimal calcularPrecioFinal() {
        BigDecimal precioBase = getPrecio();
        BigDecimal recargo = BigDecimal.ZERO;

        if (pesoKg != null && pesoKg.compareTo(BigDecimal.ONE) > 0) {
            recargo = precioBase.multiply(new BigDecimal("0.10"));
        }

        return precioBase.add(recargo).setScale(MONETARY_SCALE, ROUNDING_MODE);
    }

    @Override
    public String obtenerDetalles() {
        return "Físico - Stock: " + stock + ", Peso: " + pesoKg + " kg - Dimensiones: " + dimensiones;
    }
}
