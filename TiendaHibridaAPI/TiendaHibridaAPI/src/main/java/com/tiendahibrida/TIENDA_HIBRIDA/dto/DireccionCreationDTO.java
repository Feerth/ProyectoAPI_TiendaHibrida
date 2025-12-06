package com.tiendahibrida.TIENDA_HIBRIDA.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class DireccionCreationDTO {

    @NotBlank(message = "La calle no puede estar vacía")
    @Size(max = 200)
    private String calle;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(max = 100)
    private String ciudad;

    @NotBlank(message = "El código postal no puede estar vacío")
    @Size(max = 20)
    private String codigoPostal;
}