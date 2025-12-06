package com.tiendahibrida.TIENDA_HIBRIDA.repository;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para la entidad {@link Categoria}.
 * <p>
 * Proporciona métodos de consulta específicos como validación de unicidad del
 * nombre y búsqueda case-insensitive.
 * </p>
 *
 * Extiende {@link JpaRepository} para heredar las operaciones CRUD.
 *
 * @author Fernando y Sebastian
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Verifica si existe una categoría cuyo nombre coincida ignorando mayúsculas/minúsculas.
     *
     * @param nombre nombre a validar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Busca una categoría por nombre exacto ignorando mayúsculas/minúsculas.
     *
     * @param nombre nombre de la categoría.
     * @return Optional con la categoría encontrada.
     */
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
