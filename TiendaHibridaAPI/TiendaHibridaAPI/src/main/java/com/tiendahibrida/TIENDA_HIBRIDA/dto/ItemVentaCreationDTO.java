package com.tiendahibrida.TIENDA_HIBRIDA.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data; // Asegúrate de tener Lombok en tu proyecto

@Data
public class ItemVentaCreationDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    private int cantidad;
}