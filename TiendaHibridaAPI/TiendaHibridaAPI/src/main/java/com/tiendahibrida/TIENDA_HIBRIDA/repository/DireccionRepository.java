package com.tiendahibrida.TIENDA_HIBRIDA.repository;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para la entidad {@link Direccion}.
 * <p>
 * No incluye métodos personalizados debido a la simplicidad de la entidad,
 * pero permite CRUD completo mediante {@link JpaRepository}.
 * </p>
 *
 * @author Fernando y Sebastian
 */
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
}
