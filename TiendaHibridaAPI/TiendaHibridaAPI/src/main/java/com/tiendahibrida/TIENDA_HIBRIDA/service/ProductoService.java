package com.tiendahibrida.TIENDA_HIBRIDA.service;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Producto;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoFisico;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio principal para la gestión de productos (físicos y digitales).
 * Define las operaciones necesarias para consultar, validar y calcular
 * precios finales incluyendo descuentos o recargos según tipo.
 *
 * @author Fernando
 */
public interface ProductoService {

    /**
     * Guarda o actualiza un producto.
     *
     * @param producto entidad.
     * @return producto persistido.
     */
    Producto guardar(Producto producto);

    /**
     * Obtiene un producto por su ID.
     *
     * @param id identificador.
     * @return Optional con resultado.
     */
    Optional<Producto> obtenerPorId(Long id);

    /**
     * Lista todos los productos registrados.
     *
     * @return lista de productos.
     */
    List<Producto> listarTodos();

    /**
     * Busca productos por nombre (búsqueda parcial).
     *
     * @param termino texto a buscar.
     * @return lista encontrada.
     */
    List<Producto> buscarPorNombre(String termino);

    /**
     * Busca productos por nombre de categoría.
     *
     * @param nombreCategoria nombre de la categoría.
     * @return lista de productos.
     */
    List<Producto> buscarPorCategoria(String nombreCategoria);

    /**
     * Obtiene productos físicos cuyo stock esté por debajo del límite.
     *
     * @param limite cantidad límite.
     * @return lista.
     */
    List<ProductoFisico> obtenerProductosConStockBajo(int limite);

    /**
     * Calcula el precio final del producto aplicando lógica propia de su tipo.
     *
     * @param productoId identificador del producto.
     * @return precio final.
     */
    BigDecimal calcularPrecioFinal(Long productoId);

    /**
     * Elimina un producto.
     *
     * @param id identificador.
     */
    void eliminar(Long id);
}
