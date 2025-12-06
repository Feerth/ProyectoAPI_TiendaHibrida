package com.tiendahibrida.TIENDA_HIBRIDA.service.impl;

import com.tiendahibrida.TIENDA_HIBRIDA.model.Categoria;
import com.tiendahibrida.TIENDA_HIBRIDA.repository.CategoriaRepository;
import com.tiendahibrida.TIENDA_HIBRIDA.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de entidades {@link Categoria}.
 *
 * Proporciona operaciones CRUD y validaciones de negocio, como evitar
 * categorías duplicadas por nombre.
 *
 * <p>Incluye manejo transaccional para garantizar la consistencia
 * de los datos durante operaciones de escritura.</p>
 *
 * @author Fernando
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Constructor que inyecta el repositorio de categorías.
     *
     * @param categoriaRepository repositorio JPA para manejar categorías
     */
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Guarda o actualiza una categoría.
     * Valida que no exista otra categoría con el mismo nombre.
     *
     * @param categoria categoría a guardar
     * @return categoría persistida
     * @throws DataIntegrityViolationException si el nombre ya está registrado
     */
    @Override
    @Transactional
    public Categoria guardar(Categoria categoria) {
        Optional<Categoria> existing = categoriaRepository.findByNombreIgnoreCase(categoria.getNombre());

        if (existing.isPresent() && (categoria.getId() == null || !existing.get().getId().equals(categoria.getId()))) {
            throw new DataIntegrityViolationException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }

        return categoriaRepository.save(categoria);
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id identificador de categoría
     * @return categoría encontrada o vacío si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Busca una categoría por nombre exacto (ignora mayúsculas/minúsculas).
     *
     * @param nombre nombre a buscar
     * @return categoría encontrada o vacío si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> obtenerPorNombre(String nombre) {
        return categoriaRepository.findByNombreIgnoreCase(nombre);
    }

    /**
     * Lista todas las categorías registradas.
     *
     * @return lista de categorías
     */
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    /**
     * Elimina una categoría por ID.
     *
     * @param id identificador
     * @throws EntityNotFoundException si no existe
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoría con ID " + id + " no encontrada.");
        }
        categoriaRepository.deleteById(id);
    }
}
