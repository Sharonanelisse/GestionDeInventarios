package com.smarroquin.gestiondeinventarios.Service;

import com.smarroquin.gestiondeinventarios.models.Categoria;
import com.smarroquin.gestiondeinventarios.models.Movimiento;
import com.smarroquin.gestiondeinventarios.models.Producto;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DashboardService {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

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

    public Map<String, Integer> productosPorCategoria() {
        EntityManager em = emf.createEntityManager();
        try {
            // Consulta: contar productos agrupados por nombre de categoría
            List<Object[]> resultados = em.createQuery(
                    "SELECT p.categoria.nombre, COUNT(p) FROM Producto p GROUP BY p.categoria.nombre",
                    Object[].class
            ).getResultList();

            Map<String, Integer> datos = new HashMap<>();
            for (Object[] fila : resultados) {
                String categoria = (String) fila[0];
                Long cantidad = (Long) fila[1];
                datos.put(categoria, cantidad.intValue());
            }
            return datos;
        } finally {
            em.close();
        }
    }

    public Map<String, Integer> movimientosPorTipo() {
        EntityManager em = emf.createEntityManager();
        try {
              List<Object[]> resultados = em.createQuery(
                    "SELECT m.tipo, COUNT(m) FROM Movimiento m GROUP BY m.tipo",
                    Object[].class
            ).getResultList();

            Map<String, Integer> datos = new HashMap<>();
            for (Object[] fila : resultados) {
                String tipo = (String) fila[0];
                Long cantidad = (Long) fila[1];
                datos.put(tipo, cantidad.intValue());
            }
            return datos;
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

            cq.select(root.get("fecha"));
            cq.orderBy(cb.desc(root.get("fecha")));

            return em.createQuery(cq).setMaxResults(1).getSingleResult();
        } finally {
            em.close();
        }
    }
}