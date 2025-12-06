package com.tiendahibrida.TIENDA_HIBRIDA.repository;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para la entidad {@link Cliente}.
 * <p>
 * Incluye búsquedas específicas como por email
 * y una consulta con fetch para evitar problemas de Lazy Loading.
 * </p>
 *
 * @author Fernando y Sebastian
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su email.
     *
     * @param email email del cliente.
     * @return Optional con el cliente si existe.
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Verifica si un email ya se encuentra registrado.
     *
     * @param email email a validar.
     * @return true si existe, false si no.
     */
    boolean existsByEmail(String email);

    /**
     * Obtiene un cliente con carga explícita de su dirección e historial de ventas.
     *
     * @param id identificador del cliente.
     * @return Optional con el cliente y sus relaciones.
     */
    @Query("""
            SELECT c FROM Cliente c
            LEFT JOIN FETCH c.direccion
            LEFT JOIN FETCH c.ventas
            WHERE c.id = :id
            """)
    Optional<Cliente> findByIdConTodo(Long id);
}
