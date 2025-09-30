package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.models.Producto;
import com.smarroquin.gestiondeinventarios.models.Movimiento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class DashboardController {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

    public long totalProductos() {
        EntityManager em = emf.createEntityManager();
        long count = em.createQuery("SELECT COUNT(p) FROM Producto p", Long.class).getSingleResult();
        em.close();
        return count;
    }

    public long productosStockBajo(int umbral) {
        EntityManager em = emf.createEntityManager();
        long count = em.createQuery("SELECT COUNT(p) FROM Producto p WHERE p.stockActual < :umbral", Long.class)
                .setParameter("umbral", umbral)
                .getSingleResult();
        em.close();
        return count;
    }

    public long productosInactivos() {
        EntityManager em = emf.createEntityManager();
        long count = em.createQuery("SELECT COUNT(p) FROM Producto p WHERE p.activo = false", Long.class)
                .getSingleResult();
        em.close();
        return count;
    }

    public long totalCategorias() {
        EntityManager em = emf.createEntityManager();
        long count = em.createQuery("SELECT COUNT(c) FROM Categoria c", Long.class).getSingleResult();
        em.close();
        return count;
    }

    public long movimientosSemana() {
        EntityManager em = emf.createEntityManager();
        LocalDateTime hace7dias = LocalDateTime.now().minusDays(7);
        long count = em.createQuery("SELECT COUNT(m) FROM Movimiento m WHERE m.fecha >= :desde", Long.class)
                .setParameter("desde", hace7dias)
                .getSingleResult();
        em.close();
        return count;
    }

    public LocalDateTime ultimaActualizacion() {
        EntityManager em = emf.createEntityManager();
        LocalDateTime fecha = em.createQuery("SELECT MAX(m.fecha) FROM Movimiento m", LocalDateTime.class)
                .getSingleResult();
        em.close();
        return fecha;
    }
}
