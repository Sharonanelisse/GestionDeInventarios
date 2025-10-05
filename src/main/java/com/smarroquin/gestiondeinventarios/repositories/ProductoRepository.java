package com.smarroquin.gestiondeinventarios.repositories;

import com.smarroquin.gestiondeinventarios.models.Producto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProductoRepository {

    @PersistenceContext(unitName = "GestionDeInventarios")
    private EntityManager em;

    public List<Producto> listarTodos() {
        return em.createQuery("SELECT p FROM Producto p", Producto.class)
                .getResultList();
    }

    public void guardar(Producto producto) {
        if (producto.getId() == null) {
            em.persist(producto);
        } else {
            em.merge(producto);
        }
    }

    public void eliminar(Long id) {
        Producto p = em.find(Producto.class, id);
        if (p != null) em.remove(p);
    }
}


