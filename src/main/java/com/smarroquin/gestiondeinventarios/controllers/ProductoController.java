package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.models.Producto;
import com.smarroquin.gestiondeinventarios.repositories.ProductoRepository;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class ProductoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ProductoRepository productoRepository;


    public List<Producto> listarTodos() {
        return productoRepository.listarTodos();
    }

    public void guardar(Producto producto) {
        productoRepository.guardar(producto);
    }

    public void eliminar(Long id) {
        productoRepository.eliminar(id);
    }

    public List<Producto> buscarConFiltros(String nombre, String categoria,
                                           Double precioMin, Double precioMax,
                                           Boolean activo, Date desde, Date hasta) {
        return productoRepository.buscarConFiltros(nombre, categoria, precioMin, precioMax, activo, desde, hasta);
    }
}
