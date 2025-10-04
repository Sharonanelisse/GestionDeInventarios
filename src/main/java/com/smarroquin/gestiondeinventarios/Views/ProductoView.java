package com.smarroquin.gestiondeinventarios.Views;

import com.smarroquin.gestiondeinventarios.models.Producto;
import com.smarroquin.gestiondeinventarios.Service.ProductoService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("productoView")
@RequestScoped
public class ProductoView implements Serializable {

    private List<Producto> productos;
    private Producto productoSeleccionado;
    private String nombreFiltro;
    private String categoriaFiltro;
    private Double precioMinFiltro;
    private Double precioMaxFiltro;
    private Boolean activoFiltro;
    private java.util.Date desdeFiltro;
    private java.util.Date hastaFiltro;

    @Inject
    private ProductoService productoService;

    @PostConstruct
    public void init() {
        System.out.println("ProductoService inyectado: " + productoService);
        productos = productoService.listarTodos();
    }


    public void buscar() {
        productos = productoService.buscarConFiltros(nombreFiltro, categoriaFiltro,
                precioMinFiltro, precioMaxFiltro, activoFiltro, desdeFiltro, hastaFiltro);
    }

    public void nuevo() {
        productoSeleccionado = new Producto();
    }

    public void editar(Producto producto) {
        this.productoSeleccionado = producto;
    }

    public void guardar() {
        productoService.guardar(productoSeleccionado);
        productos = productoService.listarTodos();
    }

    public void eliminar(Producto producto) {
        productoService.eliminar(producto.getId());
        productos = productoService.listarTodos();
    }

    // Getters y Setters
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

    public java.util.Date getDesdeFiltro() { return desdeFiltro; }
    public void setDesdeFiltro(java.util.Date desdeFiltro) { this.desdeFiltro = desdeFiltro; }

    public java.util.Date getHastaFiltro() { return hastaFiltro; }
    public void setHastaFiltro(java.util.Date hastaFiltro) { this.hastaFiltro = hastaFiltro; }
}
