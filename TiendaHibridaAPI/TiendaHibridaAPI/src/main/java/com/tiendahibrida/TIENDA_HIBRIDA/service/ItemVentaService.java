package com.tiendahibrida.TIENDA_HIBRIDA.service;

import com.tiendahibrida.TIENDA_HIBRIDA.model.ItemVenta;

import java.util.Optional;

/**
 * Servicio para gestión de ítems de venta.
 * Aunque se expone como capa de servicio, generalmente es utilizado
 * internamente por VentaService debido al fuerte acoplamiento.
 *
 * @author Fernando
 */
public interface ItemVentaService {

    /**
     * Obtiene un item de venta por su ID.
     *
     * @param id identificador.
     * @return Optional con resultado.
     */
    Optional<ItemVenta> obtenerPorId(Long id);

    /**
     * Guarda o actualiza un ítem.
     *
     * @param item ItemVenta a persistir.
     * @return item guardado.
     */
    ItemVenta guardar(ItemVenta item);

    /**
     * Elimina un ítem de venta.
     *
     * @param id identificador.
     */
    void eliminar(Long id);
}
