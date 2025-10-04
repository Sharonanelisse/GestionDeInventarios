package com.smarroquin.gestiondeinventarios.Service;

import com.smarroquin.gestiondeinventarios.models.Producto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ProductoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public List<Producto> listarTodos() {
        return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
    }

    public void guardar(Producto producto) {
        if (producto.getId() == null) {
            em.persist(producto);
        } else {
            em.merge(producto);
        }
    }

    public List<Producto> buscarConFiltros(String nombre, String categoria,
                                           Double precioMin, Double precioMax,
                                           Boolean activo, java.util.Date desde, java.util.Date hasta) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM Producto p WHERE 1=1");

        if (nombre != null && !nombre.isEmpty()) {
            jpql.append(" AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))");
        }
        if (categoria != null && !categoria.isEmpty()) {
            jpql.append(" AND LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :categoria, '%'))");
        }
        if (precioMin != null) {
            jpql.append(" AND p.precio >= :precioMin");
        }
        if (precioMax != null) {
            jpql.append(" AND p.precio <= :precioMax");
        }
        if (activo != null) {
            jpql.append(" AND p.activo = :activo");
        }
        if (desde != null) {
            jpql.append(" AND p.fechaCreacion >= :desde");
        }
        if (hasta != null) {
            jpql.append(" AND p.fechaCreacion <= :hasta");
        }

        var query = em.createQuery(jpql.toString(), Producto.class);

        if (nombre != null && !nombre.isEmpty()) query.setParameter("nombre", nombre);
        if (categoria != null && !categoria.isEmpty()) query.setParameter("categoria", categoria);
        if (precioMin != null) query.setParameter("precioMin", precioMin);
        if (precioMax != null) query.setParameter("precioMax", precioMax);
        if (activo != null) query.setParameter("activo", activo);
        if (desde != null) query.setParameter("desde", desde);
        if (hasta != null) query.setParameter("hasta", hasta);

        return query.getResultList();
    }

    public void eliminar(Long id) {
        Producto p = em.find(Producto.class, id);
        if (p != null) {
            em.remove(p);
        }
    }
}

