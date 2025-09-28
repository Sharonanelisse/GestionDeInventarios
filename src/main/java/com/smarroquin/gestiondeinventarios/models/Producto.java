package com.smarroquin.gestiondeinventarios.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stockActual = 0;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Producto() {}

    public Producto(Long id, String nombre, Double precio, Integer stockActual) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stockActual = stockActual;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.categoria = new Categoria();
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public Double getPrecio() {return precio;}
    public void setPrecio(Double precio) {this.precio = precio;}

    public Integer getStockActual() {return stockActual;}
    public void setStockActual(Integer stockActual) {this.stockActual = stockActual;}

    public boolean getActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}
    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}

    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}
}
