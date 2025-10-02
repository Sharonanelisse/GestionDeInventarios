package com.smarroquin.gestiondeinventarios.Service;

import com.smarroquin.gestiondeinventarios.models.Categoria;
import com.smarroquin.gestiondeinventarios.models.Movimiento;
import com.smarroquin.gestiondeinventarios.models.Producto;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;

public class DashboardService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

    public long totalProductos() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Producto> root = cq.from(Producto.class);
            cq.select(cb.count(root));
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    public long productosStockBajo(int umbral) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Producto> root = cq.from(Producto.class);
            cq.select(cb.count(root))
                    .where(cb.lessThan(root.get("stockActual"), umbral));
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    public long productosInactivos() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Producto> root = cq.from(Producto.class);
          cq.select(cb.count(root))
                    .where(cb.isFalse(root.get("activo")));
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    public long totalCategorias() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Categoria> root = cq.from(Categoria.class);
            cq.select(cb.count(root));
            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    public long movimientosSemana() {
        EntityManager em = emf.createEntityManager();
        try {
            LocalDateTime hace7dias = LocalDateTime.now().minusDays(7);
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Movimiento> root = cq.from(Movimiento.class);

            cq.select(cb.count(root))
                    .where(cb.greaterThanOrEqualTo(root.get("fecha"), hace7dias));

            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }

    public LocalDateTime ultimaActualizacion() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<LocalDateTime> cq = cb.createQuery(LocalDateTime.class);
            Root<Movimiento> root = cq.from(Movimiento.class);

            cq.select(cb.greatest(root.get("fecha"))); // MAX(fecha)

            return em.createQuery(cq).getSingleResult();
        } finally {
            em.close();
        }
    }
}
