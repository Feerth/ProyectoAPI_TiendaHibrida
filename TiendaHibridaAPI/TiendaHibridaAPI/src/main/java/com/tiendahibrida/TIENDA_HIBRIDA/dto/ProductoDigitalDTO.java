package com.tiendahibrida.TIENDA_HIBRIDA.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoDigitalDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    @NotBlank(message = "El enlace de descarga es obligatorio")
    @Size(max = 500, message = "El enlace de descarga no puede exceder 500 caracteres")
    private String enlaceDescarga;

    @Min(value = 1, message = "El tamaño debe ser al menos 1 MB")
    private int tamanoMb;

    @Size(max = 100, message = "La licencia no puede exceder 100 caracteres")
    private String licencia;

    // Getters y Setters

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public String getEnlaceDescarga() { return enlaceDescarga; }
    public void setEnlaceDescarga(String enlaceDescarga) { this.enlaceDescarga = enlaceDescarga; }

    public int getTamanoMb() { return tamanoMb; }
    public void setTamanoMb(int tamanoMb) { this.tamanoMb = tamanoMb; }

    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
}
