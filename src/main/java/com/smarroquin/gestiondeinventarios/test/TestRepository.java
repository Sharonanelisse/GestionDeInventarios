package com.smarroquin.gestiondeinventarios.test;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class TestRepository {

    @PersistenceContext(unitName = "GestionDeInventarios")
    private EntityManager em;

    public boolean testConexion() {
        try {
            em.createQuery("SELECT 1").getSingleResult();
            System.out.println("✅ EntityManager funciona correctamente.");
            return true;
        } catch (Exception e) {
            System.err.println("❌ EntityManager sigue siendo null: " + e.getMessage());
            return false;
        }
    }
}
