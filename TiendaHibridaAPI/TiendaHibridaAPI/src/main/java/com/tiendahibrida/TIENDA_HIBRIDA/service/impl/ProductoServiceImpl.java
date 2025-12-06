package com.tiendahibrida.TIENDA_HIBRIDA.service.impl;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Producto;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoFisico;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.CategoriaRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ProductoRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de productos.
 *
 * <p>Funcionalidad:
 * <ul>
 *   <li>Crear/actualizar productos (verificando categoría asociada).</li>
 *   <li>Listar, buscar por nombre, buscar por categoría y obtener stock bajo.</li>
 *   <li>Calcular precio final delegando la lógica a la entidad {@link Producto}.</li>
 * </ul>
 *
 * <p>Consideraciones importantes:
 * <ul>
 *   <li>El método {@code guardar} obliga a que el producto tenga una categoría con id válido;
 *       se carga la categoría completa desde la BD para evitar inconsistencias (referencia detach/merge).</li>
 *   <li>El cálculo de precio final delega en la entidad {@code Producto::calcularPrecioFinal}.</li>
 * </ul>
 *
 * @author Fernando
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Guarda (crea o actualiza) un producto.
     *
     * <p>Validaciones y comportamiento:
     * <ul>
     *   <li>Si {@code producto.getCategoria()} es nula o no tiene id se lanza
     *       {@link IllegalArgumentException}.</li>
     *   <li>Se carga la categoría desde BD para asignarla a la entidad producto antes de persistir.</li>
     * </ul>
     *
     * @param producto entidad producto
     * @return producto guardado
     * @throws IllegalArgumentException si no se proporciona categoría válida
     * @throws EntityNotFoundException  si la categoría indicada no existe
     */
    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("El producto debe tener una categoría válida asociada.");
        }

        // Cargar la categoría completa desde la BD para mantener integridad referencial
        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría con ID " + producto.getCategoria().getId() + " no encontrada."));

        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

    /**
     * Lista todos los productos.
     *
     * @return lista de productos
     */
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * Obtiene un producto por id.
     *
     * @param id identificador del producto
     * @return Optional con el producto si existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Busca productos cuyo nombre contenga el término (case-insensitive).
     *
     * @param termino término a buscar en el nombre
     * @return lista de productos coincidentes
     */
    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String termino) {
        return productoRepository.findByNombreContainingIgnoreCase(termino);
    }

    /**
     * Busca productos por nombre de categoría (case-insensitive).
     *
     * <p>Si no existe la categoría, retorna lista vacía.
     *
     * @param nombreCategoria nombre de la categoría
     * @return lista de productos pertenecientes a la categoría
     */
    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(String nombreCategoria) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findByNombreIgnoreCase(nombreCategoria);
        return categoriaOpt.map(productoRepository::findByCategoria).orElse(List.of());
    }

    /**
     * Obtiene los productos físicos con stock menor o igual al límite indicado.
     *
     * @param limite valor numérico que define "stock bajo"
     * @return lista de {@link ProductoFisico} con stock bajo
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductoFisico> obtenerProductosConStockBajo(int limite) {
        return productoRepository.findProductosConStockBajo(limite);
    }

    /**
     * Calcula el precio final de un producto delegando en la entidad {@link Producto}.
     *
     * @param productoId id del producto
     * @return precio final calculado
     * @throws EntityNotFoundException si el producto no existe
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularPrecioFinal(Long productoId) {
        return productoRepository.findById(productoId)
                .map(Producto::calcularPrecioFinal)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + productoId + " no encontrado."));
    }

    /**
     * Elimina un producto por id (verifica existencia previa).
     *
     * @param id identificador del producto a eliminar
     * @throws EntityNotFoundException si no existe el producto
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto con ID " + id + " no encontrado.");
        }
        productoRepository.deleteById(id);
    }
}
