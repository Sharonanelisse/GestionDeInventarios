package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.models.Producto;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductoController {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

    public Producto crear(Producto producto) {
        validarProducto(producto);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(producto);
        em.getTransaction().commit();
        em.close();
        return producto;
    }

    public Producto actualizar(Producto producto) {
        validarProducto(producto);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Producto actualizado = em.merge(producto);
        em.getTransaction().commit();
        em.close();
        return actualizado;
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Producto p = em.find(Producto.class, id);
        if (p != null) {
            em.remove(p);
        }
        em.getTransaction().commit();
        em.close();
    }

    public Producto buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        Producto producto = em.find(Producto.class, id);
        em.close();
        return producto;
    }

    // Listado con filtros dinámicos
    public List<Producto> buscarConFiltros(String nombre, Long categoriaId, Double precioMin, Double precioMax,
                                           Boolean activo, LocalDateTime desde, LocalDateTime hasta) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
        Root<Producto> root = cq.from(Producto.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nombre != null && !nombre.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }
        if (categoriaId != null) {
            predicates.add(cb.equal(root.get("categoria").get("id"), categoriaId));
        }
        if (precioMin != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), precioMin));
        }
        if (precioMax != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("precio"), precioMax));
        }
        if (activo != null) {
            predicates.add(cb.equal(root.get("activo"), activo));
        }
        if (desde != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fechaCreacion"), desde));
        }
        if (hasta != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fechaCreacion"), hasta));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        List<Producto> resultados = em.createQuery(cq).getResultList();
        em.close();
        return resultados;
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (producto.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
