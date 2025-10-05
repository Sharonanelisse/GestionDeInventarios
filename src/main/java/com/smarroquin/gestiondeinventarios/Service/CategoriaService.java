package com.smarroquin.gestiondeinventarios.Service;

import com.smarroquin.gestiondeinventarios.models.Categoria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ApplicationScoped
public class CategoriaService implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");


    public Categoria crear(Categoria categoria) {
        validarCategoria(categoria);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
            return categoria;
        } catch (Exception e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
            throw new RuntimeException("Error al crear categoría", e);
        } finally {
            em.close();
        }
    }

    public Categoria actualizar(Categoria categoria) {
        validarCategoria(categoria);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Categoria actualizado = em.merge(categoria);
            em.getTransaction().commit();
            return actualizado;
        } catch (Exception e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            throw new RuntimeException("Error al actualizar categoría", e);
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Categoria cat = em.find(Categoria.class, id);
            if (cat != null) {
                em.remove(cat);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Optional<Categoria> buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Categoria.class, id));
        } finally {
            em.close();
        }
    }

    public List<Categoria> listar() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Categoria> cq = cb.createQuery(Categoria.class);
            Root<Categoria> root = cq.from(Categoria.class);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    // 🔹 Ejemplo de búsqueda dinámica por nombre (Criteria API)
    public List<Categoria> buscarPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Categoria> cq = cb.createQuery(Categoria.class);
            Root<Categoria> root = cq.from(Categoria.class);

            cq.select(root).where(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
    }
}

