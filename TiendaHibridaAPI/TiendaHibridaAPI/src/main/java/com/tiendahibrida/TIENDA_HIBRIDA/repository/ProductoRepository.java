package com.tiendahibrida.TIENDA_HIBRIDA.repository;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Producto;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoFisico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la jerarquía de la entidad abstracta {@link Producto}.
 * <p>
 * Incluye consultas adicionales para búsqueda por nombre, categoría y
 * detención de productos físicos con stock bajo.
 * </p>
 *
 * @author Fernando y Sebastian
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos cuyo nombre contenga parcial o totalmente
     * una cadena (ignorando mayúsculas/minúsculas).
     *
     * @param nombre término a buscar.
     * @return lista de productos coincidentes.
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Obtiene productos pertenecientes a una categoría específica.
     *
     * @param categoria categoría buscada.
     * @return lista de productos.
     */
    List<Producto> findByCategoria(Categoria categoria);

    /**
     * Obtiene productos físicos cuyo stock sea menor o igual al límite dado.
     *
     * @param limite cantidad máxima de stock permitida.
     * @return lista de productos físicos con stock bajo.
     */
    @Query("SELECT p FROM ProductoFisico p WHERE p.stock <= :limite")
    List<ProductoFisico> findProductosConStockBajo(@Param("limite") int limite);
}
