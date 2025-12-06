package com.tiendahibrida.TIENDA_HIBRIDA.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class ClienteCreationDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 150)
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email incorrecto")
    @Size(max = 100)
    private String email;

    @NotNull(message = "La dirección no puede ser nula")
    @Valid
    private DireccionCreationDTO direccion;
}