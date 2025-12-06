package com.tiendahibrida.TIENDA_HIBRIDA;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.model.ProductoDigital;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.CategoriaRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testFindByCategoria() {
        Categoria cat = new Categoria(null, "Software", "Digital", null);
        cat = categoriaRepository.save(cat);

        ProductoDigital pd = new ProductoDigital();
        pd.setNombre("Licencia");
        pd.setPrecio(new BigDecimal("50.00"));
        pd.setCategoria(cat);
        pd.setTamanoMb(10);
        productoRepository.save(pd);

        List<?> porCat = productoRepository.findByCategoria(cat);
        assertThat(porCat).isNotEmpty();
    }
}
