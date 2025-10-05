package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.models.Producto;
import com.smarroquin.gestiondeinventarios.repositories.ProductoRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class ProductoController {

    @Inject
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
}

