package com.smarroquin.gestiondeinventarios.test;

import com.smarroquin.gestiondeinventarios.models.Categoria;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

@Stateless
public class TestRepository {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionDeInventarios");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Categoria c = new Categoria("Test", "Conexión ok");
            em.persist(c);
            em.getTransaction().commit();
            System.out.println("✅ Conexión exitosa y categoría insertada con ID: " + c.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
