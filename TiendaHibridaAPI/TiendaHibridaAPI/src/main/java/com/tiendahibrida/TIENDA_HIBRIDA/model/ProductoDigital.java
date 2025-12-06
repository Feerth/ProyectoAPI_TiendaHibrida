package com.tiendahibrida.TIENDA_HIBRIDA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Representa un producto digital tal como software, archivos descargables,
 * imágenes, videos o licencias.
 *
 * <p>Atributos adicionales:</p>
 * <ul>
 *     <li>Enlace de descarga</li>
 *     <li>Tamaño del archivo</li>
 *     <li>Clave de licencia (opcional)</li>
 * </ul>
 *
 * <p>Regla de precio:</p>
 * Se aplica un 5% de descuento al precio base.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ProductoDigital extends Producto {

    @Column(length = 500)
    private String enlaceDescarga;

    private int tamanoMb;

    @Column(length = 100)
    private String licencia;

    /**
     * Aplica un descuento del 5% al precio base.
     */
    @Override
    public BigDecimal calcularPrecioFinal() {
        BigDecimal factorDescuento = new BigDecimal("0.95");
        return getPrecio()
                .multiply(factorDescuento)
                .setScale(MONETARY_SCALE, ROUNDING_MODE);
    }

    @Override
    public String obtenerDetalles() {
        return "Digital - " + tamanoMb + "MB - Licencia: " + (licencia != null ? licencia : "N/A");
    }
}
