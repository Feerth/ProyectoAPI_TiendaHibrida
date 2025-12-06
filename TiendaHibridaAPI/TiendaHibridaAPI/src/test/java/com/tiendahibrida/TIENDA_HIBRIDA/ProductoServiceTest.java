package com.tiendahibrida.TIENDA_HIBRIDA;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Producto;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoDigital;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.CategoriaRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ProductoRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.service.impl.ProductoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la lógica de negocio de ProductoService.
 */
@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Categoria categoriaValida;
    private ProductoDigital productoDigital;

    @BeforeEach
    void setUp() {
        categoriaValida = new Categoria(10L, "Software", "Digital", null);

        // Inicializar productoDigital con sus campos
        productoDigital = new ProductoDigital();
        productoDigital.setId(null);
        productoDigital.setNombre("Licencia IDE");
        productoDigital.setPrecio(new BigDecimal("100.00"));
        productoDigital.setCategoria(categoriaValida);
        productoDigital.setTamanoMb(50);
        productoDigital.setEnlaceDescarga("link");
        productoDigital.setLicencia("ABC");
    }

    // --- TEST 1: Guardar producto con categoría válida ---
    @Test
    void testGuardar_CategoriaValida_DebeGuardarExitosamente() {
        // ARRANGE
        when(categoriaRepository.findById(10L)).thenReturn(Optional.of(categoriaValida));

        ProductoDigital productoGuardado = new ProductoDigital();
        productoGuardado.setId(1L);
        productoGuardado.setNombre(productoDigital.getNombre());
        productoGuardado.setPrecio(productoDigital.getPrecio());
        productoGuardado.setCategoria(categoriaValida);
        productoGuardado.setTamanoMb(productoDigital.getTamanoMb());
        productoGuardado.setEnlaceDescarga(productoDigital.getEnlaceDescarga());
        productoGuardado.setLicencia(productoDigital.getLicencia());

        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        // ACT
        Producto result = productoService.guardar(productoDigital);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoriaRepository, times(1)).findById(10L);
        verify(productoRepository, times(1)).save(productoDigital);
    }

    // --- TEST 2: Guardar producto con categoría inexistente ---
    @Test
    void testGuardar_CategoriaInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        Categoria categoriaInvalida = new Categoria(99L, "Invalida", "Invalida", null);
        productoDigital.setCategoria(categoriaInvalida);

        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            productoService.guardar(productoDigital);
        });

        verify(productoRepository, never()).save(any(Producto.class));
    }

    // --- TEST 3: Cálculo del precio final ---
    @Test
    void testCalcularPrecioFinal_ProductoExiste_DebeRetornarPrecioCalculado() {
        // ARRANGE
        BigDecimal precioEsperado = new BigDecimal("95.00"); // 100.00 * 0.95

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoDigital));

        // ACT
        BigDecimal resultado = productoService.calcularPrecioFinal(1L);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(precioEsperado.compareTo(resultado) == 0);
    }

    // --- TEST 4: Guardar producto sin categoría asociada (Cobertura de IF) ---
    @Test
    void testGuardar_CategoriaNula_DebeLanzarIllegalArgumentException() {
        // ARRANGE
        productoDigital.setCategoria(null);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            productoService.guardar(productoDigital);
        }, "Se esperaba IllegalArgumentException por categoría nula.");

        verify(categoriaRepository, never()).findById(anyLong());
    }

    // --- TEST 5: Eliminar producto inexistente (Cobertura de EXISTS) ---
    @Test
    void testEliminar_ProductoInexistente_DebeLanzarEntityNotFoundException() {
        // ARRANGE
        Long idInexistente = 999L;
        when(productoRepository.existsById(idInexistente)).thenReturn(false);

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            productoService.eliminar(idInexistente);
        }, "Se esperaba EntityNotFoundException al intentar eliminar un producto que no existe.");

        verify(productoRepository, never()).deleteById(anyLong());
    }

    // --- TEST 6: Calcular precio de producto inexistente (Cobertura de Optional.orElseThrow) ---
    @Test
    void testCalcularPrecioFinal_ProductoInexistente_DebeLanzarEntityNotFoundException() {
        // ARRANGE
        Long idInexistente = 999L;
        when(productoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            productoService.calcularPrecioFinal(idInexistente);
        }, "Se esperaba EntityNotFoundException al calcular precio de un producto que no existe.");
    }
}