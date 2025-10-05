package com.smarroquin.gestiondeinventarios.controllers;

import com.smarroquin.gestiondeinventarios.models.Categoria;
import com.smarroquin.gestiondeinventarios.Service.CategoriaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CategoriaController implements Serializable {

    @Inject
    private CategoriaService categoriaService;

    private List<Categoria> categorias;
    private Categoria categoriaSeleccionada;
    private String query;

    @PostConstruct
    public void init() {
        cargarCategorias();
    }

    public void cargarCategorias() {
        this.categorias = categoriaService.listar();
    }

    public void nuevo() {
        this.categoriaSeleccionada = new Categoria();
    }

    public void guardar() {
        try {
            if (this.categoriaSeleccionada.getId() == null) {
                categoriaService.crear(this.categoriaSeleccionada);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Categoría creada"));
            } else {
                categoriaService.actualizar(this.categoriaSeleccionada);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Categoría actualizada"));
            }
            cargarCategorias();
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formCatalog:table");
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formCatalog:msgs");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void eliminar(Long id) {
        try {
            categoriaService.eliminar(id);
            cargarCategorias();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Categoría eliminada"));
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formCatalog:table");
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formCatalog:msgs");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void buscarPorNombre() {
        if (query != null && !query.trim().isEmpty()) {
            this.categorias = categoriaService.buscarPorNombre(query);
        } else {
            cargarCategorias();
        }
    }

    // Getters y Setters
    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Categoria getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
