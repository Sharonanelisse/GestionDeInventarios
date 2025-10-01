package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.models.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class CategoriaController {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");

    public Categoria crear(Categoria categoria) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(categoria);
        em.getTransaction().commit();
        em.close();
        return categoria;
    }

    public Categoria actualizar(Categoria categoria) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Categoria actualizado = em.merge(categoria);
        em.getTransaction().commit();
        em.close();
        return actualizado;
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Categoria cat = em.find(Categoria.class, id);
        if (cat != null) {
            em.remove(cat);
        }
        em.getTransaction().commit();
        em.close();
    }

    public Categoria buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        Categoria categoria = em.find(Categoria.class, id);
        em.close();
        return categoria;
    }

    public List<Categoria> listar() {
        EntityManager em = emf.createEntityManager();
        List<Categoria> categorias = em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
        em.close();
        return categorias;
    }
}
