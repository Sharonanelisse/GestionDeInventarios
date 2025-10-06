package com.smarroquin.gestiondeinventarios.views;

import com.smarroquin.gestiondeinventarios.controllers.ProductoController;
import com.smarroquin.gestiondeinventarios.models.Producto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Named("productoView")
@ViewScoped
public class ProductoView implements Serializable {

    private static final long serialVersionUID = 1L;

    public ProductoView() {
        System.out.println("Constructor de ProductoView ejecutado");
    }

    @Inject
    private ProductoController productoController;

    private List<Producto> productos;
    private Producto productoSeleccionado;

    private String nombreFiltro;
    private String categoriaFiltro;
    private Double precioMinFiltro;
    private Double precioMaxFiltro;
    private Boolean activoFiltro;
    private Date desdeFiltro;
    private Date hastaFiltro;

    private int pageSize = 10;

    @PostConstruct
    public void init() {
        try {
            productos = productoController.listarTodos();
            System.out.println("ProductoView inicializado correctamente con " + productos.size() + " productos.");
        } catch (Exception e) {
            System.err.println("Error al inicializar ProductoView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void buscar() {
        productos = productoController.buscarConFiltros(
                nombreFiltro, categoriaFiltro,
                precioMinFiltro, precioMaxFiltro,
                activoFiltro, desdeFiltro, hastaFiltro
        );
    }

    public void nuevo() {
        productoSeleccionado = new Producto();
    }

    public void editar(Producto producto) {
        this.productoSeleccionado = producto;
    }

    public void guardar() {
        productoController.guardar(productoSeleccionado);
        productos = productoController.listarTodos();
    }

    public void eliminar(Producto producto) {
        productoController.eliminar(producto.getId());
        productos = productoController.listarTodos();
    }

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
