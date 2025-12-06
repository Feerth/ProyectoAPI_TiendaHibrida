package com.tiendahibrida.TIENDA_HIBRIDA;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Direccion;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testGuardarYBuscarPorEmail() {
        Direccion direccion = new Direccion(null, "Av Test 1", "Ciudad", "12345", null);
        Cliente cliente = new Cliente(null, "Test Client", "test@example.com", direccion, null);

        Cliente guardado = clienteRepository.save(cliente);

        assertThat(guardado).isNotNull();
        assertThat(guardado.getId()).isNotNull();

        Optional<Cliente> encontrado = clienteRepository.findByEmail("test@example.com");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Test Client");
    }
}
