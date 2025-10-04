package com.smarroquin.gestiondeinventarios;

import com.smarroquin.gestiondeinventarios.controllers.ProductoController;
import com.smarroquin.gestiondeinventarios.controllers.CategoriaController;
import com.smarroquin.gestiondeinventarios.controllers.MovimientoController;
import com.smarroquin.gestiondeinventarios.controllers.DashboardController;

import com.smarroquin.gestiondeinventarios.models.Producto;
import com.smarroquin.gestiondeinventarios.models.Categoria;
import com.smarroquin.gestiondeinventarios.models.Movimiento;
import com.smarroquin.gestiondeinventarios.models.TipoMovimiento;

import java.time.LocalDateTime;

public class TestControladores {
    public static void main(String[] args) {
        ProductoController productoController = new ProductoController();
        CategoriaController categoriaController = new CategoriaController();
        MovimientoController movimientoController = new MovimientoController();
        DashboardController dashboardController = new DashboardController();

        System.out.println("=== Test Dashboard ===");
        System.out.println("Total productos: " + dashboardController.getTotalProductos());

        System.out.println("\n=== Test Categorías ===");
        Categoria cat = new Categoria();
        cat.setNombre("Electrónica");
        cat.setDescripcion("Dispositivos electrónicos");
        categoriaController.crear(cat);
        System.out.println("Categoría creada: " + cat.getNombre());

        System.out.println("\n=== Test Productos ===");
        Producto p = new Producto();
        p.setNombre("Laptop");
        p.setPrecio(1200.0);
        p.setStockActual(10);
        p.setCategoria(cat);
        p.setActivo(true);
        p.setFechaCreacion(LocalDateTime.now());
        productoController.crear(p);
        System.out.println("Producto creado: " + productoController.buscarPorId(p.getId()).getNombre());

        System.out.println("\n=== Test Movimientos ===");
        Movimiento m = new Movimiento();
        m.setTipo(TipoMovimiento.ENTRADA);
        m.setCantidad(5);
        m.setMotivo("Ingreso inicial");
        m.setFecha(LocalDateTime.now());
        m.setProducto(p);
        movimientoController.registrar(m);
        System.out.println("Movimiento registrado: " + m.getMotivo());
    }
}