package com.smarroquin.gestiondeinventarios.test;

import com.smarroquin.gestiondeinventarios.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;

public class DataInitializer {

    private static final String PERSISTENCE_UNIT = "GestionDeInventarios";

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // ------------------ CATEGORÍAS ------------------
            List<Categoria> categoria = List.of(
                    new Categoria("Electrónica", "Dispositivos electrónicos y accesorios."),
                    new Categoria("Hogar", "Artículos para el hogar y cocina."),
                    new Categoria("Deportes", "Equipos y accesorios deportivos."),
                    new Categoria("Oficina", "Material de oficina y papelería."),
                    new Categoria("Juguetes", "Juguetes educativos y recreativos.")
            );

            categoria.forEach(em::persist);
            em.flush();
            System.out.println("✅ Categorías creadas: " + categoria.size());

            // ------------------ PRODUCTOS ------------------
            Producto p1 = new Producto();
            p1.setNombre("Laptop Lenovo IdeaPad 3");
            p1.setPrecio(4500.00);
            p1.setStockActual(15);
            p1.setCategoria(categoria.get(0));

            Producto p2 = new Producto();
            p2.setNombre("Televisor Samsung 50\"");
            p2.setPrecio(3800.00);
            p2.setStockActual(8);
            p2.setCategoria(categoria.get(0));

            Producto p3 = new Producto();
            p3.setNombre("Licuadora Oster 10 velocidades");
            p3.setPrecio(750.00);
            p3.setStockActual(20);
            p3.setCategoria(categoria.get(1));

            Producto p4 = new Producto();
            p4.setNombre("Silla ergonómica ejecutiva");
            p4.setPrecio(1200.00);
            p4.setStockActual(12);
            p4.setCategoria(categoria.get(3));

            Producto p5 = new Producto();
            p5.setNombre("Balón de fútbol Adidas");
            p5.setPrecio(250.00);
            p5.setStockActual(30);
            p5.setCategoria(categoria.get(2));

            Producto p6 = new Producto();
            p6.setNombre("Lápiz Faber Castell HB2");
            p6.setPrecio(2.50);
            p6.setStockActual(500);
            p6.setCategoria(categoria.get(3));

            Producto p7 = new Producto();
            p7.setNombre("Rompecabezas 1000 piezas");
            p7.setPrecio(180.00);
            p7.setStockActual(25);
            p7.setCategoria(categoria.get(4));

            List<Producto> producto = List.of(p1, p2, p3, p4, p5, p6, p7);
            producto.forEach(em::persist);
            em.flush();
            System.out.println("✅ Productos creados: " + producto.size());

            // ------------------ MOVIMIENTOS ------------------
            List<Movimiento> movimiento = List.of(
                    new Movimiento(TipoMovimiento.ENTRADA, 10, "Reposición inicial", LocalDate.now().minusDays(5), p1),
                    new Movimiento(TipoMovimiento.ENTRADA, 5, "Ingreso nuevos televisores", LocalDate.now().minusDays(3), p2),
                    new Movimiento(TipoMovimiento.SALIDA, 2, "Venta cliente", LocalDate.now().minusDays(2), p1),
                    new Movimiento(TipoMovimiento.SALIDA, 1, "Venta tienda", LocalDate.now().minusDays(1), p3),
                    new Movimiento(TipoMovimiento.ENTRADA, 50, "Compra mayorista lápices", LocalDate.now().minusDays(7), p6),
                    new Movimiento(TipoMovimiento.SALIDA, 5, "Entrega oficina", LocalDate.now(), p6),
                    new Movimiento(TipoMovimiento.ENTRADA, 15, "Nuevo lote rompecabezas", LocalDate.now(), p7)
            );

            movimiento.forEach(em::persist);
            em.flush();
            System.out.println("✅ Movimientos creados: " + movimiento.size());

            // ------------------ ACTUALIZAR STOCK ------------------
            for (Producto productos : producto) {
                int stockCalculado = em.createQuery(
                                "SELECT COALESCE(SUM(CASE WHEN m.tipo = :entrada THEN m.cantidad ELSE -m.cantidad END), 0) " +
                                        "FROM Movimiento m WHERE m.producto = :producto", Integer.class)
                        .setParameter("entrada", TipoMovimiento.ENTRADA)
                        .setParameter("producto", producto)
                        .getSingleResult();
                productos.setStockActual(stockCalculado);
                em.merge(producto);
            }

            em.getTransaction().commit();
            System.out.println("🎉 Base de datos inicializada correctamente.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
