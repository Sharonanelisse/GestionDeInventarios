package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.Service.DashboardService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Named
@ViewScoped
public class DashboardController implements Serializable {

    @Inject
    private DashboardService dashboardService;

    private long totalProductos;
    private long productosStockBajo;
    private long productosInactivos;
    private long totalCategorias;
    private long movimientosSemana;
    private LocalDateTime ultimaActualizacion;

    private String productosPorCategoriaChart;
    private String movimientosPorTipoChart;

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

    public String getProductosPorCategoriaChart() {
        return productosPorCategoriaChart;
    }

    public String getMovimientosPorTipoChart() {
        return movimientosPorTipoChart;
    }

    private void crearGraficoProductosPorCategoria() {
        Map<String, Integer> datos = dashboardService.productosPorCategoria();
        productosPorCategoriaChart = generarModelo(datos, "Productos");
    }

    private void crearGraficoMovimientosPorTipo() {
        Map<String, Integer> datos = dashboardService.movimientosPorTipo();
        movimientosPorTipoChart = generarModelo(datos, null);
    }

    private String generarModelo(Map<String, Integer> datos, String label) {
        StringBuilder labels = new StringBuilder("[");
        StringBuilder values = new StringBuilder("[");
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            labels.append("\"").append(entry.getKey()).append("\",");
            values.append(entry.getValue()).append(",");
        }
        if (!datos.isEmpty()) {
            labels.setLength(labels.length() - 1);
            values.setLength(values.length() - 1);
        }
        labels.append("]");
        values.append("]");

        if (label != null) {
            return String.format("""
            {
              "labels": %s,
              "datasets": [{
                "label": "%s",
                "data": %s
              }]
            }
            """, labels, label, values);
        } else {
            return String.format("""
            {
              "labels": %s,
              "datasets": [{
                "data": %s
              }]
            }
            """, labels, values);
        }
    }
}
