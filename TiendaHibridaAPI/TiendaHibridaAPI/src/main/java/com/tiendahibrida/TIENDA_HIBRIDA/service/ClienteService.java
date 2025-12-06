package com.tiendahibrida.TIENDA_HIBRIDA.service;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la administración de clientes.
 * Gestiona registro, edición, validación de email, y eliminación segura
 * considerando ventas asociadas.
 *
 * @author Fernando
 */
public interface ClienteService {

    /**
     * Crea o actualiza un cliente.
     * Debe validar que el email sea único.
     *
     * @param cliente entidad a persistir.
     * @return cliente guardado.
     */
    Cliente guardar(Cliente cliente);

    /**
     * Obtiene un cliente por ID.
     *
     * @param id identificador.
     * @return Optional con el cliente.
     */
    Optional<Cliente> obtenerPorId(Long id);

    /**
     * Busca un cliente por email.
     *
     * @param email email del cliente.
     * @return Optional con resultado.
     */
    Optional<Cliente> obtenerPorEmail(String email);

    /**
     * Obtiene todos los clientes.
     *
     * @return lista de clientes.
     */
    List<Cliente> listarTodos();

    /**
     * Elimina un cliente.
     * Debe validar que no existan ventas asignadas.
     *
     * @param id identificador.
     */
    void eliminar(Long id);
}
