package com.tiendahibrida.TIENDA_HIBRIDA.service.impl;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ClienteRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio encargado de la gestión de clientes.
 *
 * <p>Incluye validaciones como evitar correos duplicados, inicializar
 * correctamente la relación con la dirección y exponer operaciones CRUD.</p>
 *
 * @author Fernando
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Constructor con inyección del repositorio de clientes.
     *
     * @param clienteRepository repositorio JPA para clientes
     */
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Guarda o actualiza un cliente, validando que su email no esté duplicado.
     *
     * @param cliente cliente a guardar
     * @return cliente persistido
     * @throws DataIntegrityViolationException si el email ya existe
     */
    @Override
    @Transactional
    public Cliente guardar(Cliente cliente) {
        Optional<Cliente> existing = clienteRepository.findByEmail(cliente.getEmail());

        if (existing.isPresent() && (cliente.getId() == null || !existing.get().getId().equals(cliente.getId()))) {
            throw new DataIntegrityViolationException("El email " + cliente.getEmail() + " ya está registrado.");
        }

        // Ajusta relación OneToOne bidireccional
        if (cliente.getDireccion() != null) {
            cliente.getDireccion().setCliente(cliente);
        }

        return clienteRepository.save(cliente);
    }

    /**
     * Obtiene un cliente por ID, incluyendo relaciones LAZY según configuración.
     *
     * @param id identificador de cliente
     * @return cliente encontrado o vacío si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findByIdConTodo(id);
    }

    /**
     * Busca un cliente por su correo electrónico.
     *
     * @param email correo del cliente
     * @return cliente encontrado o vacío si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    /**
     * Lista todos los clientes registrados.
     *
     * @return lista de clientes
     */
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    /**
     * Elimina un cliente por ID.
     *
     * @param id identificador
     * @throws EntityNotFoundException si el cliente no existe
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente con ID " + id + " no encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}
