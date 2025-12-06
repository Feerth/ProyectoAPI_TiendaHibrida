package com.tiendahibrida.TIENDA_HIBRIDA.controller;

import com.tiendahibrida.TIENDA_HIBRIDA.dto.ProductoDigitalDTO;
import com.tiendahibrida.TIENDA_HIBRIDA.dto.ProductoFisicoDTO;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Producto;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoDigital;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoFisico;
import com.tiendahibrida.TIENDA_HIBRIDA.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Productos", description = "Operaciones sobre productos físicos y digitales")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // ---------------- Crear productos genéricos ----------------
    @Operation(summary = "Crear o actualizar un producto")
    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto creado = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PostMapping(value = "/digitales", consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Producto> crearDigital(@RequestBody ProductoDigitalDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setId(dto.getCategoriaId());

        ProductoDigital pd = ProductoDigital.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .categoria(categoria)
                .enlaceDescarga(dto.getEnlaceDescarga())
                .tamanoMb(dto.getTamanoMb())
                .licencia(dto.getLicencia())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.guardar(pd));
    }

    @PostMapping(value = "/fisicos", consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Producto> crearFisico(@RequestBody ProductoFisicoDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setId(dto.getCategoriaId());

        ProductoFisico pf = ProductoFisico.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .categoria(categoria)
                .stock(dto.getStock())
                .pesoKg(dto.getPesoKg())
                .dimensiones(dto.getDimensiones())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.guardar(pf));
    }

    // ---------------- Obtener productos ----------------
    @Operation(summary = "Obtener producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos o filtrar por nombre y tipo
    @Operation(summary = "Buscar productos por nombre, tipo o listar todos")
    @GetMapping
    public ResponseEntity<List<Producto>> buscarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo) {

        // Primero obtenemos los productos por nombre o todos si nombre es nulo
        List<Producto> productos;
        if (nombre != null && !nombre.isEmpty()) {
            productos = productoService.buscarPorNombre(nombre);
        } else {
            productos = productoService.listarTodos();
        }

        // Filtrar por tipo si se proporciona
        if (tipo != null && !tipo.isEmpty()) {
            if (tipo.equalsIgnoreCase("fisico")) {
                productos = productos.stream()
                        .filter(p -> p instanceof ProductoFisico)
                        .toList(); // mantener List<Producto>
            } else if (tipo.equalsIgnoreCase("digital")) {
                productos = productos.stream()
                        .filter(p -> p instanceof ProductoDigital)
                        .toList();
            }
        }

        return ResponseEntity.ok(productos);
    }


    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(nombre));
    }

    @GetMapping("/stock/bajo")
    public ResponseEntity<List<ProductoFisico>> stockBajo(@RequestParam(defaultValue = "5") int limite) {
        return ResponseEntity.ok(productoService.obtenerProductosConStockBajo(limite));
    }

    @GetMapping("/{id}/precio-final")
    public ResponseEntity<BigDecimal> precioFinal(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.calcularPrecioFinal(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Listar solo por tipo ----------------
    @GetMapping("/fisicos")
    public ResponseEntity<List<ProductoFisico>> listarFisicos() {
        List<ProductoFisico> lista = productoService.listarTodos().stream()
                .filter(p -> p instanceof ProductoFisico)
                .map(p -> (ProductoFisico) p)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/digitales")
    public ResponseEntity<List<ProductoDigital>> listarDigitales() {
        List<ProductoDigital> lista = productoService.listarTodos().stream()
                .filter(p -> p instanceof ProductoDigital)
                .map(p -> (ProductoDigital) p)
                .toList();
        return ResponseEntity.ok(lista);
    }
}
