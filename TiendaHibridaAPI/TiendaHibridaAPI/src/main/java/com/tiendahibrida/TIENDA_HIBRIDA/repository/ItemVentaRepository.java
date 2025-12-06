package com.tiendahibrida.TIENDA_HIBRIDA.repository;

import com.tiendahibrida.TIENDA_HIBRIDA.model.ItemVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para la entidad {@link ItemVenta}.
 * <p>
 * Permite la persistencia de ítems individuales en una venta.
 * Usualmente trabajado indirectamente por {@link com.tiendahibrida.TIENDA_HIBRIDA.service.VentaService}.
 * </p>
 *
 * @author Fernando y Sebastian
 */
@Repository
public interface ItemVentaRepository extends JpaRepository<ItemVenta, Long> {
}
