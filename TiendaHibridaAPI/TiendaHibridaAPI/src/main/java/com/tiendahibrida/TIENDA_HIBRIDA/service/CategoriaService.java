package com.tiendahibrida.TIENDA_HIBRIDA.service;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de categorías de productos.
 * Define las operaciones CRUD y reglas de negocio necesarias para
 * garantizar integridad de datos (como nombre único).
 *
 * Este servicio abstrae la capa repository y mantiene la lógica de negocio.
 *
 * @author Fernando
 */
public interface CategoriaService {

    /**
     * Crea una nueva categoría o actualiza una existente.
     * Debe validar que no exista otra categoría con el mismo nombre.
     *
     * @param categoria entidad a guardar.
     * @return categoría persistida.
     */
    Categoria guardar(Categoria categoria);

    /**
     * Obtiene una categoría según su ID.
     *
     * @param id identificador único.
     * @return Optional con la categoría encontrada.
     */
    Optional<Categoria> obtenerPorId(Long id);

    /**
     * Busca una categoría por su nombre (ignora mayúsculas y minúsculas).
     *
     * @param nombre nombre de la categoría.
     * @return Optional con la categoría si existe.
     */
    Optional<Categoria> obtenerPorNombre(String nombre);

    /**
     * Lista todas las categorías registradas.
     *
     * @return lista de categorías.
     */
    List<Categoria> listarTodas();

    /**
     * Elimina una categoría por su ID.
     * Debe validar que no existan productos asociados.
     *
     * @param id identificador a eliminar.
     */
    void eliminar(Long id);
}
