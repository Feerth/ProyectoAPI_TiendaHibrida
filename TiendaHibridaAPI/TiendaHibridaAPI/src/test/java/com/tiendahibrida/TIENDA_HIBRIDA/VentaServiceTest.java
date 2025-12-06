package com.tiendahibrida.TIENDA_HIBRIDA;

import com.tiendahibrida.TIENDA_HIBRIDA.model.*;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.*;
import com.tiendahibrida.TIENDA_HIBRIDA.service.impl.VentaServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la lógica de negocio crítica de VentaService.
 */
@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Cliente clienteValido;
    private ProductoFisico productoFisicoConStock;

    @BeforeEach
    void setUp() {
        // ARRANGE global: Configurar entidades base simuladas
        clienteValido = new Cliente(1L, "Test Client", "test@mail.com", null, new HashSet<>());

        // Producto físico con stock inicial de 10 unidades
        productoFisicoConStock = new ProductoFisico();
        productoFisicoConStock.setId(2L);
        productoFisicoConStock.setNombre("Monitor 4K");
        productoFisicoConStock.setPrecio(new BigDecimal("500.00"));
        productoFisicoConStock.setStock(10);
        productoFisicoConStock.setPesoKg(new BigDecimal("0.5"));
    }

    // --- TEST 1: Venta exitosa con Producto Físico (Descuento de Stock) ---
    @Test
    void testFinalizarVenta_StockSuficiente_DebeReducirStockYGuardar() {
        // ARRANGE
        Venta venta = new Venta();
        venta.setCliente(clienteValido);
        ItemVenta itemFisico = new ItemVenta(productoFisicoConStock, 3);
        venta.agregarItem(itemFisico);

        // CORRECCIÓN: Usar findById para simular la búsqueda del objeto completo
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(productoFisicoConStock));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        // ACT
        Venta result = ventaService.finalizarVenta(venta);

        // ASSERT
        assertNotNull(result);
        assertEquals(7, productoFisicoConStock.getStock(), "El stock debe haberse reducido de 10 a 7.");
        verify(productoRepository, times(1)).save(productoFisicoConStock);
        verify(ventaRepository, times(1)).save(venta);
    }

    // --- TEST 2: Venta Falla por Stock Insuficiente (Garantizar Rollback) ---
    @Test
    void testFinalizarVenta_StockInsuficiente_DebeLanzarExcepcionYNoGuardar() {
        // ARRANGE
        Venta venta = new Venta();
        venta.setCliente(clienteValido);
        ItemVenta itemFisico = new ItemVenta(productoFisicoConStock, 15);
        venta.agregarItem(itemFisico);

        // CORRECCIÓN: Usar findById para simular la búsqueda exitosa del cliente
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(productoFisicoConStock));

        // ACT & ASSERT: Esperamos la RuntimeException que encapsula el error de stock
        assertThrows(RuntimeException.class, () -> {
            ventaService.finalizarVenta(venta);
        }, "Se esperaba una excepción de stock insuficiente.");

        verify(ventaRepository, never()).save(any(Venta.class));
        assertEquals(10, productoFisicoConStock.getStock(), "El stock no debe cambiar tras la excepción.");
    }

    // --- TEST 3: Venta Falla si el Cliente no existe ---
    @Test
    void testFinalizarVenta_ClienteNoExiste_DebeLanzarEntityNotFound() {
        // ARRANGE
        Venta venta = new Venta();
        Cliente clienteInvalido = new Cliente(99L, "Bad Client", "bad@mail.com", null, new HashSet<>());
        venta.setCliente(clienteInvalido);

        // CORRECCIÓN: Usar findById y devolver Optional.empty()
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            ventaService.finalizarVenta(venta);
        }, "Se esperaba una excepción de Cliente no encontrado.");

        verify(productoRepository, never()).findById(anyLong());
    }

    // --- TEST 4: Venta Falla si el Cliente es Nulo (Cobertura de IF) ---
    @Test
    void testFinalizarVenta_ClienteNulo_DebeLanzarEntityNotFound() {
        // ARRANGE
        Venta venta = new Venta(); // Cliente es nulo por defecto

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            ventaService.finalizarVenta(venta);
        }, "Se esperaba una excepción de Cliente nulo/no válido.");

        // Se verifica que la lógica no llegó al repository
        verify(clienteRepository, never()).findById(anyLong());
    }

    // --- TEST 5: Venta Falla si el Producto no existe (Cobertura de Optional.isEmpty) ---
    @Test
    void testFinalizarVenta_ProductoInexistente_DebeLanzarEntityNotFound() {
        // ARRANGE
        Venta venta = new Venta();
        venta.setCliente(clienteValido);

        Producto productoInexistente = new ProductoDigital();
        productoInexistente.setId(999L);
        productoInexistente.setPrecio(new BigDecimal("1.00"));

        ItemVenta itemInexistente = new ItemVenta(productoInexistente, 1);
        venta.agregarItem(itemInexistente);

        // CORRECCIÓN: Usar findById para simular el éxito en la búsqueda del cliente
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));

        // Mock que fuerza el fallo en el producto
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            ventaService.finalizarVenta(venta);
        }, "Se esperaba una excepción porque el producto no existe.");

        verify(ventaRepository, never()).save(any(Venta.class));
    }
}