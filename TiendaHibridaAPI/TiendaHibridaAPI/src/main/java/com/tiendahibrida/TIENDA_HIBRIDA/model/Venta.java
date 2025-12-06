package com.tiendahibrida.TIENDA_HIBRIDA.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Representa una venta registrada en el sistema.
 *
 * <p>Componentes:</p>
 * <ul>
 *     <li>Fecha/hora de venta</li>
 *     <li>Cliente asociado</li>
 *     <li>Lista de ítems vendidos</li>
 * </ul>
 *
 * <p>Notas:</p>
 * - La fecha se asigna automáticamente antes de persistirse.
 * - La relación con items es bidireccional.
 * - El total se calcula dinámicamente asegurando consistencia.
 */
@Entity
@Table(name = "venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cliente", "items"})
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora de la venta.
     * Se establece automáticamente al persistir.
     */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /**
     * Cliente que realizó la compra.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"ventas", "direccion"})
    private Cliente cliente;

    /**
     * Lista de items relacionados a la venta.
     * Cascade.ALL permite persistir cada item al guardar la venta.
     */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemVenta> items = new ArrayList<>();

    /**
     * Establece automáticamente la fecha si no ha sido definida.
     */
    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

    /**
     * Agrega un item a la venta y establece la relación bidireccional.
     */
    public void agregarItem(ItemVenta item) {
        if (item == null) return;
        items.add(item);
        item.setVenta(this);
    }

    /**
     * Elimina un item de la venta.
     */
    public void removerItem(ItemVenta item) {
        if (item == null) return;
        items.remove(item);
        item.setVenta(null);
    }

    /**
     * Calcula el total de la venta sumando los subtotales de los items.
     */
    @Transient
    public BigDecimal getTotal() {
        Optional<BigDecimal> total = items.stream()
                .map(ItemVenta::getSubtotal)
                .reduce(BigDecimal::add);

        return total.orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
