package com.tiendahibrida.TIENDA_HIBRIDA.controller;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ItemVenta;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Producto;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Venta;
import com.tiendahibrida.TIENDA_HIBRIDA.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.tiendahibrida.TIENDA_HIBRIDA.dto.VentaCreationDTO; // 👈 NUEVA IMPORTACIÓN
import com.tiendahibrida.TIENDA_HIBRIDA.dto.ItemVentaCreationDTO; // 👈 NUEVA IMPORTACIÓN
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Tag(name = "Ventas", description = "Operaciones para registrar y consultar ventas")
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Finalizar (registrar) una venta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venta registrada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Venta.class),
                            examples = @ExampleObject(value = "{ \"cliente\": {\"id\":1}, \"items\": [ {\"producto\": {\"id\":1}, \"cantidad\":2}, {\"producto\": {\"id\":2}, \"cantidad\":1} ] }")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Venta> finalizarVenta(@Valid @RequestBody VentaCreationDTO dto) {
        // 1. Mapear DTO a Entidad Venta (solo con IDs de cliente y producto)
        Venta venta = new Venta();

        // Asignar Cliente (solo el ID, el servicio se encarga de buscarlo)
        Cliente cliente = new Cliente();
        cliente.setId(dto.getClienteId());
        venta.setCliente(cliente);

        // Mapear Items
        for (ItemVentaCreationDTO itemDto : dto.getItems()) {

            // Creamos un objeto Producto con solo el ID, para que el servicio lo busque
            // Nota: Este objeto debe ser una instancia de Producto o una de sus subclases
            Producto producto = new Producto() {
                // Implementación dummy de métodos abstractos necesaria para instanciar la clase abstracta
                @Override public BigDecimal calcularPrecioFinal() { return BigDecimal.ZERO; }
                @Override public String obtenerDetalles() { return ""; }
            };
            producto.setId(itemDto.getProductoId());

            // Creamos el ItemVenta
            ItemVenta item = new ItemVenta();
            item.setCantidad(itemDto.getCantidad());
            item.setProducto(producto);

            venta.agregarItem(item);
        }

        Venta registrada = ventaService.finalizarVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrada);
    }

    @Operation(summary = "Obtener venta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        return ventaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todas las ventas")
    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @Operation(summary = "Buscar ventas por fecha (rango de días, formato yyyy-MM-dd)")
    @GetMapping("/buscar")
    public ResponseEntity<List<Venta>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        LocalDateTime desdeInicio = desde.atStartOfDay();
        LocalDateTime hastaFin = LocalDateTime.of(hasta, LocalTime.MAX);

        return ResponseEntity.ok(ventaService.buscarPorRango(desdeInicio, hastaFin));
    }

    @Operation(summary = "Calcular total vendido entre dos fechas (yyyy-MM-dd)")
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> totalVendido(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        LocalDateTime desdeInicio = desde.atStartOfDay();
        LocalDateTime hastaFin = LocalDateTime.of(hasta, LocalTime.MAX);

        return ResponseEntity.ok(ventaService.calcularTotalVendidoEntreFechas(desdeInicio, hastaFin));
    }
}
