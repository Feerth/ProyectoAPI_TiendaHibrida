package com.tiendahibrida.TIENDA_HIBRIDA;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias para {@link CategoriaRepository}.
 * Utiliza @DataJpaTest para asegurar un entorno transaccional aislado para la DB.
 * @author Fernando y Sebastian
 */
@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testCrearYBuscarCategoria() {
        // ARRANGE: Crear objeto
        Categoria nueva = new Categoria(null, "Electrónica", "Dispositivos varios", null);

        // ACT: Guardar
        Categoria guardada = categoriaRepository.save(nueva);

        // ASSERT: Verificar Creación
        assertThat(guardada).isNotNull();
        assertThat(guardada.getId()).isNotNull();

        // ACT: Buscar por ID
        Optional<Categoria> encontrada = categoriaRepository.findById(guardada.getId());

        // ASSERT: Verificar Lectura
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNombre()).isEqualTo("Electrónica");
    }

    @Test
    void testActualizarCategoria() {
        // ARRANGE
        Categoria categoria = new Categoria(null, "Cocina", "Utensilios", null);
        Categoria guardada = categoriaRepository.save(categoria);

        // ACT
        guardada.setDescripcion("Herramientas de cocina profesionales");
        Categoria actualizada = categoriaRepository.save(guardada);

        // ASSERT
        assertThat(actualizada.getDescripcion()).isEqualTo("Herramientas de cocina profesionales");
    }

    @Test
    void testEliminarCategoria() {
        // ARRANGE
        Categoria categoria = new Categoria(null, "Deportes", "Artículos de ejercicio", null);
        Categoria guardada = categoriaRepository.save(categoria);

        // ACT
        categoriaRepository.deleteById(guardada.getId());

        // ASSERT
        Optional<Categoria> eliminada = categoriaRepository.findById(guardada.getId());
        assertThat(eliminada).isEmpty();
    }

    @Test
    void testListarTodasLasCategorias() {
        // ARRANGE
        categoriaRepository.save(new Categoria(null, "Libros", "Ficción y no ficción", null));
        categoriaRepository.save(new Categoria(null, "Ropa", "Vestuario", null));

        // ACT
        List<Categoria> categorias = categoriaRepository.findAll();

        // ASSERT
        assertThat(categorias).hasSize(2);
    }

    @Test
    void testUnicidadDeNombreFalla() {
        // ARRANGE
        categoriaRepository.save(new Categoria(null, "Juguetes", "Para niños", null));

        // ACT & ASSERT: Intentar guardar una duplicada debe fallar por DataIntegrityViolationException
        Categoria duplicada = new Categoria(null, "Juguetes", "Para adultos", null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            categoriaRepository.saveAndFlush(duplicada); // Usar saveAndFlush para forzar la ejecución en DB
        });
    }

    @Test
    void testFindByNombreIgnoreCase() {
        // ARRANGE
        categoriaRepository.save(new Categoria(null, "Hogar", "Decoración", null));

        // ACT
        Optional<Categoria> encontrada = categoriaRepository.findByNombreIgnoreCase("hogar");

        // ASSERT
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNombre()).isEqualTo("Hogar");
    }
}