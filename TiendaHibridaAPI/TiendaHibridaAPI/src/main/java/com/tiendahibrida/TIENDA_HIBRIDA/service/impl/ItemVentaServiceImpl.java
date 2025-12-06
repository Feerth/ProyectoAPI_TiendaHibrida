package com.tiendahibrida.TIENDA_HIBRIDA.service.impl;

import com.tiendahibrida.TIENDA_HIBRIDA.model.ItemVenta;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ItemVentaRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.service.ItemVentaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio encargado de gestionar los ítems de una venta.
 *
 * <p>Incluye validaciones básicas y operaciones CRUD.</p>
 */
@Service
public class ItemVentaServiceImpl implements ItemVentaService {

    private final ItemVentaRepository itemVentaRepository;

    /**
     * Constructor con inyección del repositorio.
     *
     * @param itemVentaRepository repositorio JPA de ItemVenta
     */
    public ItemVentaServiceImpl(ItemVentaRepository itemVentaRepository) {
        this.itemVentaRepository = itemVentaRepository;
    }

    /**
     * Obtiene un ítem por su ID.
     *
     * @param id identificador
     * @return ítem encontrado o vacío si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ItemVenta> obtenerPorId(Long id) {
        return itemVentaRepository.findById(id);
    }

    /**
     * Guarda un ítem de venta.
     *
     * @param item ítem a guardar
     * @return ítem persistido
     */
    @Override
    @Transactional
    public ItemVenta guardar(ItemVenta item) {
        return itemVentaRepository.save(item);
    }

    /**
     * Elimina un ítem por su ID.
     *
     * @param id identificador
     * @throws EntityNotFoundException si el ítem no existe
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!itemVentaRepository.existsById(id)) {
            throw new EntityNotFoundException("ItemVenta con ID " + id + " no encontrado.");
        }
        itemVentaRepository.deleteById(id);
    }
}
