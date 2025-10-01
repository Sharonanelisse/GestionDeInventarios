package com.smarroquin.gestiondeinventarios.Service;

import com.smarroquin.gestiondeinventarios.models.Movimiento;
import com.smarroquin.gestiondeinventarios.models.Producto;
import com.smarroquin.gestiondeinventarios.models.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

    public Movimiento registrar(Movimiento movimiento) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Producto producto = em.find(Producto.class, movimiento.getProducto().getId());
            if (producto == null) {
                throw new IllegalArgumentException("El producto no existe");
            }

            if (movimiento.getTipo() == TipoMovimiento.SALIDA) {
                if (!producto.getActivo()) {
                    throw new IllegalArgumentException("No se puede registrar salida de un producto inactivo");
                }
                if (producto.getStockActual() - movimiento.getCantidad() < 0) {
                    throw new IllegalArgumentException("La salida dejaría stock negativo");
                }
                producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
            } else if (movimiento.getTipo() == TipoMovimiento.ENTRADA) {
                producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
            }

            movimiento.setFecha(LocalDateTime.now());
            movimiento.setProducto(producto);

            em.persist(movimiento);
            em.merge(producto);

            em.getTransaction().commit();
            return movimiento;
        } finally {
            em.close();
        }
    }

    public List<Movimiento> listar() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Movimiento> cq = cb.createQuery(Movimiento.class);
            Root<Movimiento> root = cq.from(Movimiento.class);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Movimiento> filtrar(Long productoId, TipoMovimiento tipo, LocalDateTime desde, LocalDateTime hasta) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Movimiento> cq = cb.createQuery(Movimiento.class);
            Root<Movimiento> root = cq.from(Movimiento.class);

            List<Predicate> predicates = new ArrayList<>();

            if (productoId != null) {
                predicates.add(cb.equal(root.get("producto").get("id"), productoId));
            }
            if (tipo != null) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (desde != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"), desde));
            }
            if (hasta != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fecha"), hasta));
            }

            cq.select(root).where(predicates.toArray(new Predicate[0]));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}

