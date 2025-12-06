package com.tiendahibrida.TIENDA_HIBRIDA;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Direccion;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Venta;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ClienteRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.service.impl.ClienteServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la lógica de negocio de ClienteService.
 * Valida la unicidad del email, el manejo de dirección y el control de excepciones.
 *
 * @author Fernando y Sebastian
 */
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteBase;
    private Direccion direccionBase;

    @BeforeEach
    void setUp() {
        // Inicialización de entidades base
        // Direccion: (id, calle, ciudad, codigoPostal, cliente)
        direccionBase = new Direccion(1L, "Calle Test 123", "Ciudad Test", "7800000", null);

        Set<Venta> ventasVacias = new HashSet<>();
        // Cliente: (id, nombre, email, direccion, ventas)
        clienteBase = new Cliente(1L, "Juan Perez", "juan.perez@test.com", direccionBase, ventasVacias);

        // NOTA: Se eliminó el when(clienteRepository.findByEmail) de aquí para evitar UnnecessaryStubbingException
    }

    // --- TEST 1: Crear un cliente con email único (Flujo OK) ---
    @Test
    void testGuardar_EmailUnico_DebeGuardarExitosamente() {
        // ARRANGE
        // Configuración necesaria para este test: el email no debe existir
        when(clienteRepository.findByEmail(clienteBase.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteBase);

        // ACT
        Cliente guardado = clienteService.guardar(clienteBase);

        // ASSERT
        assertNotNull(guardado);
        assertEquals(1L, guardado.getId());

        verify(clienteRepository, times(1)).findByEmail(clienteBase.getEmail());
        verify(clienteRepository, times(1)).save(clienteBase);
    }

    // --- TEST 2: Intentar crear un cliente con email duplicado (Flujo Error) ---
    @Test
    void testGuardar_EmailDuplicado_DebeLanzarExcepcion() {
        // ARRANGE
        Set<Venta> ventasVacias = new HashSet<>();
        // Cliente duplicado (ID=null, mismo email)
        Cliente duplicado = new Cliente(null, "Maria Gomez", "juan.perez@test.com", null, ventasVacias);

        // Configuración necesaria para este test: el email ya existe en la DB
        when(clienteRepository.findByEmail("juan.perez@test.com")).thenReturn(Optional.of(clienteBase));

        // ACT & ASSERT: Esperamos que lance DataIntegrityViolationException
        assertThrows(DataIntegrityViolationException.class, () -> {
            clienteService.guardar(duplicado);
        }, "Se esperaba una excepción por email duplicado.");

        // Verificar que el método save NUNCA fue llamado.
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // --- TEST 3: Actualizar un cliente existente (Flujo OK) ---
    @Test
    void testGuardar_ActualizarMismoCliente_NoDebeFallar() {
        // ARRANGE
        clienteBase.setNombre("Juan Perez ACTUALIZADO");

        // Configuración necesaria para este test: encuentra el email, pero el ID es el mismo
        when(clienteRepository.findByEmail(clienteBase.getEmail())).thenReturn(Optional.of(clienteBase));
        when(clienteRepository.save(clienteBase)).thenReturn(clienteBase);

        // ACT
        Cliente actualizado = clienteService.guardar(clienteBase);

        // ASSERT
        assertNotNull(actualizado);
        assertEquals("Juan Perez ACTUALIZADO", actualizado.getNombre());

        verify(clienteRepository, times(1)).findByEmail(clienteBase.getEmail());
        verify(clienteRepository, times(1)).save(clienteBase);
    }

    // --- TEST 4: Guardar cliente con dirección nula (Cobertura de IF) ---
    @Test
    void testGuardar_DireccionNula_DebeGuardarExitosamente() {
        // ARRANGE
        Cliente clienteSinDireccion = new Cliente(null, "Pedro Nulo", "pedro.nulo@test.com", null, new HashSet<>());
        Cliente clienteGuardado = new Cliente(2L, "Pedro Nulo", "pedro.nulo@test.com", null, new HashSet<>());

        // Configuración necesaria para este test
        when(clienteRepository.findByEmail(clienteSinDireccion.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.save(clienteSinDireccion)).thenReturn(clienteGuardado);

        // ACT
        Cliente result = clienteService.guardar(clienteSinDireccion);

        // ASSERT
        assertNotNull(result);
        assertEquals(2L, result.getId());

        // El IF que chequea la dirección se cubre al pasar null.
        verify(clienteRepository, times(1)).save(clienteSinDireccion);
    }

    // --- TEST 5: Eliminar cliente inexistente (Cobertura de EXISTS) ---
    @Test
    void testEliminar_ClienteInexistente_DebeLanzarEntityNotFoundException() {
        // ARRANGE
        Long idInexistente = 999L;
        // Configuración necesaria para este test
        when(clienteRepository.existsById(idInexistente)).thenReturn(false);

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            clienteService.eliminar(idInexistente);
        }, "Se esperaba EntityNotFoundException al intentar eliminar un cliente que no existe.");

        // Verificar que la eliminación NUNCA fue llamada.
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}