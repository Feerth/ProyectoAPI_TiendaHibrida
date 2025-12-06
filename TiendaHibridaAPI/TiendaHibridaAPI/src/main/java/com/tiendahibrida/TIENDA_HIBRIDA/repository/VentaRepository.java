package com.tiendahibrida.TIENDA_HIBRIDA.repository;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para la entidad {@link Venta}.
 * <p>
 * Incluye consultas específicas para filtrado por fecha y cálculo de montos
 * totales vendidos.
 * </p>
 *
 * @autor Fernando y Sebastian
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Obtiene todas las ventas registradas entre dos fechas.
     *
     * @param inicio fecha/hora inicial.
     * @param fin    fecha/hora final.
     * @return lista de ventas.
     */
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Calcula el monto total vendido en un rango específico de fechas.
     *
     * @param inicio fecha/hora inicial.
     * @param fin    fecha/hora final.
     * @return suma total de ventas.
     */
    @Query("""
            SELECT SUM(i.precioUnitario * i.cantidad)
            FROM Venta v JOIN v.items i
            WHERE v.fecha BETWEEN :inicio AND :fin
            """)
    BigDecimal calcularTotalVendidoEntreFechas(@Param("inicio") LocalDateTime inicio,
                                               @Param("fin") LocalDateTime fin);
}
