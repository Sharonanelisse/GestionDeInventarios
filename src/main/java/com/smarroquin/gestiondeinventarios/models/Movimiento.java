package com.smarroquin.gestiondeinventarios.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;  // ENTRADA o SALIDA

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private Integer cantidad;

    private String motivo;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public Movimiento() {}

    public Movimiento(TipoMovimiento tipo, Integer cantidad, String motivo, LocalDate fecha, Producto producto) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fecha = fecha;
        this.producto = producto;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public TipoMovimiento getTipo() {return tipo;}
    public void setTipo(TipoMovimiento tipo) {this.tipo = tipo;}

    public Integer getCantidad() {return cantidad;}
    public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}

    public String getMotivo() {return motivo;}
    public void setMotivo(String motivo) {this.motivo = motivo;}

    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}

    public Producto getProducto() {return producto;}
    public void setProducto(Producto producto) {this.producto = producto;}
}
