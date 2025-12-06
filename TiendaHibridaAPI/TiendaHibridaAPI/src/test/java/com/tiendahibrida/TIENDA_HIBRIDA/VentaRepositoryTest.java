package com.tiendahibrida.TIENDA_HIBRIDA;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Cliente;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Direccion;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoDigital;
import com.tiendahibrida.TIENDA_HIBRIDA.model.Venta;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.CategoriaRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ClienteRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ProductoRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VentaRepositoryTest {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testFindByFechaBetween() {
        Categoria cat = categoriaRepository.save(new Categoria(null, "Software", "Digital", null));
        ProductoDigital pd = new ProductoDigital();
        pd.setNombre("Producto");
        pd.setPrecio(new BigDecimal("10.00"));
        pd.setCategoria(cat);
        pd = (ProductoDigital) productoRepository.save(pd);

        Cliente cliente = clienteRepository.save(new Cliente(null, "Cliente", "c@a.com", new Direccion(null, "Calle", "Ciudad", "0000", null), null));

        Venta v = new Venta();
        v.setCliente(cliente);
        v.setFecha(LocalDateTime.now());
        ventaRepository.save(v);

        List<?> res = ventaRepository.findByFechaBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        assertThat(res).isNotEmpty();
    }
}
