package com.tiendahibrida.TIENDA_HIBRIDA.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data; // Asegúrate de tener Lombok en tu proyecto

import java.util.List;

@Data
public class VentaCreationDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @Valid
    @NotEmpty(message = "La venta debe contener al menos un item")
    private List<ItemVentaCreationDTO> items;
}