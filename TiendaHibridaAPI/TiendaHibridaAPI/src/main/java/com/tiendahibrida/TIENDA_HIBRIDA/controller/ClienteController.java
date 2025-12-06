package com.tiendahibrida.TIENDA_HIBRIDA.controller;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import com.tiendahibrida.TIENDA_HIBRIDA.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Direccion;
import com.tiendahibrida.TIENDA_HIBRIDA.dto.ClienteCreationDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador encargado de administrar operaciones relacionadas con clientes.
 * Permite:
 * - Crear clientes con dirección incluida
 * - Obtener clientes por ID
 * - Listar clientes
 * - Actualizar sus datos
 * - Eliminar registros
 */
@Tag(name = "Clientes", description = "Operaciones para gestionar clientes")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Constructor con inyección del servicio de clientes.
     */
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Crea un nuevo cliente incluyendo su dirección.
     *
     * @param dto DTO que contiene los datos del cliente y su dirección.
     * @return Cliente creado y código 201.
     */
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente. Devuelve 201 CREATED.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado"),
            @ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Cliente> crear(@Valid @RequestBody ClienteCreationDTO dto) {

        // Construcción de la entidad Direccion a partir del DTO
        Direccion direccion = new Direccion();
        direccion.setCalle(dto.getDireccion().getCalle());
        direccion.setCiudad(dto.getDireccion().getCiudad());
        direccion.setCodigoPostal(dto.getDireccion().getCodigoPostal());

        // Construcción de Cliente
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setDireccion(direccion);

        Cliente creado = clienteService.guardar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id ID del cliente.
     * @return Cliente encontrado o 404.
     */
    @Operation(summary = "Obtener cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos los clientes registrados.
     *
     * @return Lista de clientes.
     */
    @Operation(summary = "Listar todos los clientes")
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id ID del cliente.
     * @param cliente Nuevos datos.
     * @return Cliente actualizado.
     */
    @Operation(summary = "Actualizar cliente")
    @PutMapping(value = "/{id}", consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        cliente.setId(id);
        return ResponseEntity.ok(clienteService.guardar(cliente));
    }

    /**
     * Elimina un cliente por ID.
     *
     * @param id ID del cliente.
     * @return 204 No Content.
     */
    @Operation(summary = "Eliminar cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
