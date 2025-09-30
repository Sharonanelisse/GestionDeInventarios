package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.models.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class MovimientoController {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

    public Movimiento registrar(Movimiento movimiento) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Producto producto = em.find(Producto.class, movimiento.getProducto().getId());

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
        em.close();
        return movimiento;
    }

    public List<Movimiento> listar() {
        EntityManager em = emf.createEntityManager();
        List<Movimiento> movimientos = em.createQuery("SELECT m FROM Movimiento m", Movimiento.class).getResultList();
        em.close();
        return movimientos;
    }

    public List<Movimiento> filtrar(Long productoId, TipoMovimiento tipo, LocalDateTime desde, LocalDateTime hasta) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT m FROM Movimiento m WHERE 1=1";
        if (productoId != null) jpql += " AND m.producto.id = :productoId";
        if (tipo != null) jpql += " AND m.tipo = :tipo";
        if (desde != null) jpql += " AND m.fecha >= :desde";
        if (hasta != null) jpql += " AND m.fecha <= :hasta";

        TypedQuery<Movimiento> query = em.createQuery(jpql, Movimiento.class);

        if (productoId != null) query.setParameter("productoId", productoId);
        if (tipo != null) query.setParameter("tipo", tipo);
        if (desde != null) query.setParameter("desde", desde);
        if (hasta != null) query.setParameter("hasta", hasta);

        List<Movimiento> resultados = query.getResultList();
        em.close();
        return resultados;
    }
}
