package com.smarroquin.gestiondeinventarios.dao;

import com.smarroquin.gestiondeinventarios.models.Producto;
import com.smarroquin.gestiondeinventarios.models.Categoria;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class ProductoDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

    public void save(Producto producto) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        producto.setFechaActualizacion(LocalDate.now());
        if (producto.getFechaCreacion() == null) {
            producto.setFechaCreacion(LocalDate.now());
        }
        em.persist(producto);
        em.getTransaction().commit();
        em.close();
    }

    public Producto update(Producto producto) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        producto.setFechaActualizacion(LocalDate.now());
        Producto updated = em.merge(producto);
        em.getTransaction().commit();
        em.close();
        return updated;
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Producto p = em.find(Producto.class, id);
        if (p != null) em.remove(p);
        em.getTransaction().commit();
        em.close();
    }

    public Producto findById(Long id) {
        EntityManager em = emf.createEntityManager();
        Producto p = em.find(Producto.class, id);
        em.close();
        return p;
    }

    // Listado con filtros, paginación y ordenamiento
    public List<Producto> findWithFilters(
            String nombre,
            Categoria categoria,
            Double precioMin,
            Double precioMax,
            Boolean activo,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            int page,
            int pageSize,
            String orderBy,
            boolean asc
    ) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
        Root<Producto> root = cq.from(Producto.class);

        Predicate p = cb.conjunction();

        if (nombre != null && !nombre.isEmpty()) {
            p = cb.and(p, cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }

        if (categoria != null) {
            p = cb.and(p, cb.equal(root.get("categoria"), categoria));
        }

        if (precioMin != null) {
            p = cb.and(p, cb.ge(root.get("precio"), precioMin));
        }

        if (precioMax != null) {
            p = cb.and(p, cb.le(root.get("precio"), precioMax));
        }

        if (activo != null) {
            p = cb.and(p, cb.equal(root.get("activo"), activo));
        }

        if (fechaDesde != null) {
            p = cb.and(p, cb.greaterThanOrEqualTo(root.get("fechaCreacion"), fechaDesde));
        }

        if (fechaHasta != null) {
            p = cb.and(p, cb.lessThanOrEqualTo(root.get("fechaCreacion"), fechaHasta));
        }

        cq.where(p);

        if (orderBy != null) {
            if (asc) cq.orderBy(cb.asc(root.get(orderBy)));
            else cq.orderBy(cb.desc(root.get(orderBy)));
        }

        TypedQuery<Producto> query = em.createQuery(cq);
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        List<Producto> results = query.getResultList();
        em.close();
        return results;
    }
}
