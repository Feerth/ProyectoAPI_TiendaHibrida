package com.tiendahibrida.TIENDA_HIBRIDA.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoFisicoDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    private BigDecimal pesoKg;

    @NotBlank(message = "Las dimensiones son obligatorias")
    @Size(max = 100, message = "Las dimensiones no pueden exceder 100 caracteres")
    private String dimensiones;

    // Getters y Setters

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }

    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }
}
