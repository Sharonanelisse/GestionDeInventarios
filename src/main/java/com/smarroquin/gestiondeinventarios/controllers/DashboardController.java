package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.service.DashboardService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

@Named
@ViewScoped
public class DashboardController implements Serializable {

    private DashboardService dashboardService;

    private long totalProductos;
    private long productosStockBajo;
    private long productosInactivos;
    private long totalCategorias;
    private long movimientosSemana;
    private LocalDateTime ultimaActualizacion;

    private BarChartModel productosPorCategoriaChart;
    private PieChartModel movimientosPorTipoChart;

    @PostConstruct
    public void init() {
        dashboardService = new DashboardService(); // Si usas CDI, puedes inyectarlo con @Inject

        totalProductos = dashboardService.totalProductos();
        productosStockBajo = dashboardService.productosStockBajo(10); // umbral configurable
        productosInactivos = dashboardService.productosInactivos();
        totalCategorias = dashboardService.totalCategorias();
        movimientosSemana = dashboardService.movimientosSemana();
        ultimaActualizacion = dashboardService.ultimaActualizacion();

        crearGraficoProductosPorCategoria();
        crearGraficoMovimientosPorTipo();
    }

    // Getters para la vista
    public long getTotalProductos() {
        return totalProductos;
    }

    public long getProductosStockBajo() {
        return productosStockBajo;
    }

    public long getProductosInactivos() {
        return productosInactivos;
    }

    public long getTotalCategorias() {
        return totalCategorias;
    }

    public long getMovimientosSemana() {
        return movimientosSemana;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public BarChartModel getProductosPorCategoriaChart() {
        return productosPorCategoriaChart;
    }

    public PieChartModel getMovimientosPorTipoChart() {
        return movimientosPorTipoChart;
    }

    // Métodos para construir los gráficos
    private void crearGraficoProductosPorCategoria() {
        productosPorCategoriaChart = new BarChartModel();

        ChartSeries productos = new ChartSeries();
        productos.setLabel("Productos");

        // Datos simulados - reemplaza con datos reales si lo deseas
        productos.set("Electrónica", 120);
        productos.set("Ropa", 80);
        productos.set("Alimentos", 150);
        productos.set("Hogar", 60);
        productos.set("Libros", 40);

        productosPorCategoriaChart.addSeries(productos);
        productosPorCategoriaChart.setTitle("Productos por Categoría");
        productosPorCategoriaChart.setLegendPosition("ne");
        productosPorCategoriaChart.setAnimate(true);
        productosPorCategoriaChart.setShowPointLabels(true);
    }

    private void crearGraficoMovimientosPorTipo() {
        movimientosPorTipoChart = new PieChartModel();

        // Datos simulados - reemplaza con datos reales si lo deseas
        movimientosPorTipoChart.set("Entrada", 200);
        movimientosPorTipoChart.set("Salida", 180);
        movimientosPorTipoChart.set("Ajuste", 20);

        movimientosPorTipoChart.setTitle("Movimientos por Tipo");
        movimientosPorTipoChart.setLegendPosition("w");
        movimientosPorTipoChart.setShowDataLabels(true);
        movimientosPorTipoChart.setFill(true);
    }
}
