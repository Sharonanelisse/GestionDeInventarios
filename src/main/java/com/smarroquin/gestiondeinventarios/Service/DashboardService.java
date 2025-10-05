package com.smarroquin.gestiondeinventarios.Service;

import com.smarroquin.gestiondeinventarios.models.Categoria;
import com.smarroquin.gestiondeinventarios.models.Movimiento;
import com.smarroquin.gestiondeinventarios.models.Producto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless // EJB administrado por WildFly
public class DashboardService {

    @PersistenceContext(unitName = "GestionDeInventarios")
    private EntityManager em;

    public long totalProductos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Producto> root = cq.from(Producto.class);
        cq.select(cb.count(root));
        return em.createQuery(cq).getSingleResult();
    }

    public Map<String, Integer> productosPorCategoria() {
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
    }

    public Map<String, Integer> movimientosPorTipo() {
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
    }

    public long productosStockBajo(int umbral) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Producto> root = cq.from(Producto.class);
        cq.select(cb.count(root)).where(cb.lessThan(root.get("stockActual"), umbral));
        return em.createQuery(cq).getSingleResult();
    }

    public long productosInactivos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Producto> root = cq.from(Producto.class);
        cq.select(cb.count(root)).where(cb.isFalse(root.get("activo")));
        return em.createQuery(cq).getSingleResult();
    }

    public long totalCategorias() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Categoria> root = cq.from(Categoria.class);
        cq.select(cb.count(root));
        return em.createQuery(cq).getSingleResult();
    }

    public long movimientosSemana() {
        LocalDateTime hace7dias = LocalDateTime.now().minusDays(7);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Movimiento> root = cq.from(Movimiento.class);
        cq.select(cb.count(root))
                .where(cb.greaterThanOrEqualTo(root.get("fecha"), hace7dias));
        return em.createQuery(cq).getSingleResult();
    }

    public LocalDateTime ultimaActualizacion() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LocalDateTime> cq = cb.createQuery(LocalDateTime.class);
        Root<Movimiento> root = cq.from(Movimiento.class);
        cq.select(root.get("fecha"));
        cq.orderBy(cb.desc(root.get("fecha")));
        TypedQuery<LocalDateTime> query = em.createQuery(cq);
        query.setMaxResults(1);
        return query.getResultStream().findFirst().orElse(null);
    }
}
