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
        System.out.println("Total productos: " + dashboardController.totalProductos());

        System.out.println("\n=== Test Categorías ===");
        Categoria cat = new Categoria(null, "Electrónica", "Dispositivos electrónicos");
        categoriaController.crear(cat);
        System.out.println("Categoría creada: " + cat.getNombre());

        System.out.println("\n=== Test Productos ===");
        Producto p = new Producto(null, "Laptop", 1200.0, 10);
        p.setCategoria(cat);
        productoController.crear(p);
        System.out.println("Producto creado: " + productoController.buscarPorId(p.getId()).getNombre());

        System.out.println("\n=== Test Movimientos ===");
        Movimiento m = new Movimiento(null, TipoMovimiento.ENTRADA, 5, "Ingreso inicial", p.getFechaCreacion());
        m.setProducto(p);
        movimientoController.registrar(m);
        System.out.println("Movimiento registrado: " + m.getMotivo());
    }
}
