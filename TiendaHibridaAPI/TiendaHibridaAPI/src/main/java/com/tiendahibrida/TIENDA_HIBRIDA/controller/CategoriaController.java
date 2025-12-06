package com.tiendahibrida.TIENDA_HIBRIDA.controller;

import com.tiendahibrida.TIENDA_HIBRIDA.dto.CategoriaCreationDTO;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con las categorías de productos dentro del sistema.
 *
 * Proporciona endpoints para:
 * - Crear una categoría
 * - Obtener una categoría por ID
 * - Listar todas las categorías
 * - Actualizar una categoría
 * - Eliminar una categoría
 */
@Tag(name = "Categorías", description = "Operaciones para gestionar categorías de productos")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Constructor para inyección de dependencias.
     */
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Endpoint para crear una nueva categoría.
     *
     * @param dto DTO que contiene los datos de creación.
     * @return Categoría creada y código 201.
     */
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría. Devuelve 201 si se crea correctamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class),
                            examples = @ExampleObject(value = "{ \"nombre\": \"Electrónica\", \"descripcion\": \"Productos electrónicos\" }")
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Nombre de categoría duplicado")
    })
    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaCreationDTO dto) {

        // Mapeo manual de DTO → Entidad
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria creada = categoriaService.guardar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**
     * Obtiene una categoría según su ID.
     *
     * @param id ID de la categoría.
     * @return Categoría encontrada o 404 si no existe.
     */
    @Operation(summary = "Obtener categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todas las categorías registradas.
     *
     * @return Lista de categorías.
     */
    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    /**
     * Actualiza los datos de una categoría existente.
     *
     * @param id ID de la categoría a actualizar.
     * @param categoria Datos nuevos.
     * @return Categoría actualizada.
     */
    @Operation(summary = "Actualizar una categoría")
    @PutMapping(value = "/{id}", consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        categoria.setId(id);
        return ResponseEntity.ok(categoriaService.guardar(categoria));
    }

    /**
     * Elimina una categoría por ID.
     *
     * @param id ID a eliminar.
     * @return 204 No Content.
     */
    @Operation(summary = "Eliminar una categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
