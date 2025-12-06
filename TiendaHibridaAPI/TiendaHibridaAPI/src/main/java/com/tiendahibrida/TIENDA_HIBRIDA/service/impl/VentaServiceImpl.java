package com.tiendahibrida.TIENDA_HIBRIDA.service.impl;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ItemVenta;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Producto;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoFisico;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Venta;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ClienteRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ProductoRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.VentaRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.service.VentaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de gestión de ventas.
 *
 * <p>Responsabilidades:
 * <ul>
 *   <li>Finalizar una venta: validar cliente, cargar productos, ajustar stock para productos físicos,
 *       fijar precio unitario de cada item y persistir la venta con sus items.</li>
 *   <li>Listar ventas y buscarlas por rango de fechas.</li>
 *   <li>Calcular total vendido entre dos fechas (delegando al repositorio).</li>
 * </ul>
 *
 * <p>Notas de implementación:
 * <ul>
 *   <li>Se fuerza la inicialización de relaciones LAZY (cliente, items, producto dentro del item)
 *       antes de devolver resultados para evitar problemas de Serialización/DTO en controladores.</li>
 *   <li>La reducción de stock se aplica sólo a instancias de {@link ProductoFisico} y se persiste
 *       para mayor claridad (aunque el contexto transaccional normalmente lo manejaría).</li>
 * </ul>
 *
 * @author Fernando
 */
@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public VentaServiceImpl(VentaRepository ventaRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Finaliza (registra) una venta.
     *
     * Flujo:
     * <ol>
     *   <li>Validar que la venta contenga un cliente (id) y cargar la entidad Cliente desde BD.</li>
     *   <li>Para cada ItemVenta: cargar Producto desde BD; si es ProductoFisico reducir stock y guardar;</li>
     *   <li>Fijar el precio unitario del item usando {@code producto.calcularPrecioFinal()} y asociar entidades.</li>
     *   <li>Guardar la venta y forzar la carga de campos LAZY necesarios para la respuesta.</li>
     * </ol>
     *
     * @param venta entidad venta con items (los productos pueden venir solo con id)
     * @return venta registrada con items y cliente completamente cargados
     * @throws EntityNotFoundException si cliente o algún producto no existe
     */
    @Override
    @Transactional
    public Venta finalizarVenta(Venta venta) {

        // 1. OBTENER Y ASIGNAR EL OBJETO CLIENTE COMPLETO
        Long clienteId = venta.getCliente() != null ? venta.getCliente().getId() : null;

        if (clienteId == null) {
            throw new EntityNotFoundException("El cliente asociado a la venta es nulo.");
        }

        // Buscar y asignar el Cliente completo desde BD (asegura nombre/email cargados)
        Cliente clienteDB = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El cliente con ID " + clienteId + " no existe."
                ));
        venta.setCliente(clienteDB);

        // 2. Procesar items y cargar productos
        for (ItemVenta item : venta.getItems()) {

            // Buscar producto en BD por id (el Item suele llevar solo id de producto)
            Producto productoDB = productoRepository.findById(item.getProducto().getId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Producto con ID " + item.getProducto().getId() + " no existe."
                            )
                    );

            // Reducir stock si es instancia de ProductoFisico y persistir cambio de stock.
            if (productoDB instanceof ProductoFisico productoFisico) {
                productoFisico.reducirStock(item.getCantidad());
                // Persistir el producto físicamente actualizado (claridad, aunque @Transactional suele bastar)
                productoRepository.save(productoFisico);
            }

            // Asignar precio unitario calculado por la entidad Producto y enlazar relaciones
            item.setPrecioUnitario(productoDB.calcularPrecioFinal());
            item.setProducto(productoDB);
            item.setVenta(venta);
        }

        // Guardar venta con items (se guardan en cascada si la entidad está configurada)
        Venta registrada = ventaRepository.save(venta);

        // 3. FORZAR CARGA LAZY (evitar LazyInitializationException en la capa de presentación)
        if (registrada.getCliente() != null) {
            registrada.getCliente().getNombre(); // acceso para inicializar proxy
        }

        return registrada;
    }

    /**
     * Obtiene una venta por id.
     *
     * @param id identificador de la venta
     * @return Optional con la venta (vacío si no existe)
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> obtenerPorId(Long id) {
        // Nota: la inicialización de campos LAZY se puede manejar aquí si fuera necesario.
        return ventaRepository.findById(id);
    }

    /**
     * Lista todas las ventas, forzando la inicialización de cliente, items y producto dentro de cada item
     * para facilitar la serialización en la capa de controlador.
     *
     * @return lista de ventas con relaciones principales inicializadas
     */
    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodas() {
        List<Venta> ventas = ventaRepository.findAll();

        // Forzar inicialización de relaciones LAZY para evitar problemas fuera del contexto transaccional
        for (Venta venta : ventas) {
            if (venta.getCliente() != null) {
                venta.getCliente().getNombre();
            }

            for (ItemVenta item : venta.getItems()) {
                if (item.getProducto() != null) {
                    item.getProducto().getNombre(); // acceso para inicializar proxy
                }
            }
        }

        return ventas;
    }

    /**
     * Busca ventas cuyo campo fecha esté entre 'desde' y 'hasta'.
     *
     * @param desde fecha/hora inicial (inclusive)
     * @param hasta fecha/hora final (inclusive)
     * @return lista de ventas en el rango
     */
    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarPorRango(LocalDateTime desde, LocalDateTime hasta) {
        return ventaRepository.findByFechaBetween(desde, hasta);
    }

    /**
     * Calcula el total vendido entre dos fechas delegando al repositorio.
     *
     * @param desde fecha/hora inicio
     * @param hasta fecha/hora fin
     * @return total vendido (BigDecimal); retorna BigDecimal.ZERO si el repositorio devuelve null
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalVendidoEntreFechas(LocalDateTime desde, LocalDateTime hasta) {
        BigDecimal total = ventaRepository.calcularTotalVendidoEntreFechas(desde, hasta);
        return total != null ? total : BigDecimal.ZERO;
    }
}
