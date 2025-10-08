package com.smarroquin.gestiondeinventarios.views;

import com.smarroquin.gestiondeinventarios.controllers.ProductoController;
import com.smarroquin.gestiondeinventarios.models.Producto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named("productoView")
@ViewScoped
public class ProductoView implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private ProductoController productoController;

    private List<Producto> productos;
    private Producto productoSeleccionado;

    // Filtros
    private String nombreFiltro;
    private String categoriaFiltro;
    private Double precioMinFiltro;
    private Double precioMaxFiltro;
    private Boolean activoFiltro;
    private Date desdeFiltro;
    private Date hastaFiltro;

    private int pageSize = 10;

    // ------------------------
    // Inicialización
    // ------------------------
    @PostConstruct
    public void init() {
        try {
            productos = productoController.listarTodos();
            if (productos == null) {
                productos = new ArrayList<>();
            }
            System.out.println("ProductoView inicializado con " + productos.size() + " productos.");
        } catch (Exception e) {
            System.err.println("Error inicializando ProductoView: " + e.getMessage());
            e.printStackTrace();
            productos = new ArrayList<>();
        }
    }

    // ------------------------
    // Métodos de acción
    // ------------------------
    public void buscar() {
        productos = productoController.buscarConFiltros(
                nombreFiltro, categoriaFiltro,
                precioMinFiltro, precioMaxFiltro,
                activoFiltro, desdeFiltro, hastaFiltro
        );
        if (productos == null) {
            productos = new ArrayList<>();
        }
    }

    public void nuevo() {
        productoSeleccionado = new Producto();
    }

    public void editar(Producto producto) {
        this.productoSeleccionado = producto;
    }

    public void guardar() {
        if (productoSeleccionado != null) {
            productoController.guardar(productoSeleccionado);
            productos = productoController.listarTodos();
        }
    }

    public void eliminar(Producto producto) {
        if (producto != null && producto.getId() != null) {
            productoController.eliminar(producto.getId());
            productos = productoController.listarTodos();
        }
    }

    // ------------------------
    // Getters y setters
    // ------------------------
    public List<Producto> getProductos() { return productos; }
    public Producto getProductoSeleccionado() { return productoSeleccionado; }
    public void setProductoSeleccionado(Producto productoSeleccionado) { this.productoSeleccionado = productoSeleccionado; }

    public String getNombreFiltro() { return nombreFiltro; }
    public void setNombreFiltro(String nombreFiltro) { this.nombreFiltro = nombreFiltro; }

    public String getCategoriaFiltro() { return categoriaFiltro; }
    public void setCategoriaFiltro(String categoriaFiltro) { this.categoriaFiltro = categoriaFiltro; }

    public Double getPrecioMinFiltro() { return precioMinFiltro; }
    public void setPrecioMinFiltro(Double precioMinFiltro) { this.precioMinFiltro = precioMinFiltro; }

    public Double getPrecioMaxFiltro() { return precioMaxFiltro; }
    public void setPrecioMaxFiltro(Double precioMaxFiltro) { this.precioMaxFiltro = precioMaxFiltro; }

    public Boolean getActivoFiltro() { return activoFiltro; }
    public void setActivoFiltro(Boolean activoFiltro) { this.activoFiltro = activoFiltro; }

    public Date getDesdeFiltro() { return desdeFiltro; }
    public void setDesdeFiltro(Date desdeFiltro) { this.desdeFiltro = desdeFiltro; }

    public Date getHastaFiltro() { return hastaFiltro; }
    public void setHastaFiltro(Date hastaFiltro) { this.hastaFiltro = hastaFiltro; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
