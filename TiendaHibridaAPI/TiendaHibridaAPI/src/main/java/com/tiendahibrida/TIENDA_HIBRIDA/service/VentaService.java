package com.tiendahibrida.TIENDA_HIBRIDA.service;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la gestión completa de ventas.
 * Centraliza reglas críticas como actualización de stock, validación
 * de disponibilidad y persistencia de ítems.
 *
 * Este servicio debe ser transaccional para asegurar consistencia.
 *
 * @author Fernando
 */
public interface VentaService {

    /**
     * Registra y finaliza una venta.
     * Debe reducir stock de productos físicos y persistir ítems correctamente.
     *
     * @param venta entidad con items y cliente asignado.
     * @return venta guardada.
     */
    Venta finalizarVenta(Venta venta);

    /**
     * Obtiene una venta mediante su ID.
     *
     * @param id identificador.
     * @return Optional con resultado.
     */
    Optional<Venta> obtenerPorId(Long id);

    /**
     * Lista todas las ventas realizadas.
     *
     * @return lista.
     */
    List<Venta> listarTodas();

    /**
     * Busca ventas en un rango de fechas.
     *
     * @param desde inicio.
     * @param hasta fin.
     * @return lista encontrada.
     */
    List<Venta> buscarPorRango(LocalDateTime desde, LocalDateTime hasta);

    /**
     * Calcula el monto total vendido en un rango.
     *
     * @param desde inicio.
     * @param hasta fin.
     * @return monto total.
     */
    BigDecimal calcularTotalVendidoEntreFechas(LocalDateTime desde, LocalDateTime hasta);
}
