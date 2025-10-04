package com.smarroquin.gestiondeinventarios.Pruebas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smarroquin.gestiondeinventarios.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

public class CargaDesdeJson {

    private static final String PERSISTENCE_UNIT = "gestiondeinventarios";
    private static final String CATEGORIA_PATH = "data/categoria.json";
    private static final String PRODUCTO_PATH = "data/producto.json";
    private static final String MOVIMIENTO_PATH = "data/movimiento.json";
    private static final int TAMANO_LOTE = 50;

    public static void main(String[] args) throws Exception {
        List<CategoriaJson> listaCategoriaJson = leerCategoriaJson();
        List<ProductoJson> listaProductoJson = leerProductoJson();
        List<MovimientoJson> listaMovimientoJson = leerMovimientoJson();

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            // Insertar categorías y construir mapa por nombre
            Map<String, Categoria> categoriasPorNombre = new HashMap<>();
            int contador = 0;
            for (CategoriaJson categoriaJson : listaCategoriaJson) {
                Categoria categoria = new Categoria();
                categoria.setNombre(categoriaJson.getNombre());
                categoria.setDescripcion(categoriaJson.getDescripcion());
                entityManager.persist(categoria);
                categoriasPorNombre.put(categoria.getNombre(), categoria);

                contador++;
                if (contador % TAMANO_LOTE == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            System.out.println("Categorías insertadas desde JSON: " + listaCategoriaJson.size());

            // Insertar productos usando categoriaNombre
            contador = 0;
            for (ProductoJson productoJson : listaProductoJson) {
                Producto producto = new Producto();
                Categoria categoria = categoriasPorNombre.get(productoJson.getCategoriaNombre());
                if (categoria == null) {
                    throw new IllegalStateException("Categoría '" + productoJson.getCategoriaNombre() + "' no encontrada.");
                }
                producto.setCategoria(categoria);
                producto.setNombre(productoJson.getNombre());
                producto.setPrecio(productoJson.getPrecio() != null ? productoJson.getPrecio() : 0.00);
                producto.setStockActual(productoJson.getStockActual() != null ? productoJson.getStockActual() : 0);
                producto.setActivo(productoJson.getActivo() != null ? productoJson.getActivo() : Boolean.TRUE);
                producto.setFechaCreacion(productoJson.getFechaCreacion() != null ? productoJson.getFechaCreacion() : LocalDate.now());
                producto.setFechaActualizacion(productoJson.getFechaActualizacion() != null ? productoJson.getFechaActualizacion() : LocalDate.now());

                entityManager.persist(producto);
                contador++;
                if (contador % TAMANO_LOTE == 0) {
                    entityManager.flush();
                }
            }
            entityManager.flush();
            System.out.println("Productos insertados desde JSON: " + listaProductoJson.size());

            // Insertar movimientos
            contador = 0;
            for (MovimientoJson movimientoJson : listaMovimientoJson) {
                Movimiento movimiento = new Movimiento();
                movimiento.setTipo(movimientoJson.getTipo());
                movimiento.setCantidad(movimientoJson.getCantidad());
                movimiento.setMotivo(movimientoJson.getMotivo());
                movimiento.setFecha(movimientoJson.getFecha() != null ? movimientoJson.getFecha() : LocalDate.now());

                Producto producto = entityManager.find(Producto.class, movimientoJson.getProductoId());
                if (producto == null) {
                    throw new IllegalStateException("Producto con ID " + movimientoJson.getProductoId() + " no encontrado.");
                }
                movimiento.setProducto(producto);

                entityManager.persist(movimiento);
                contador++;
                if (contador % TAMANO_LOTE == 0) {
                    entityManager.flush();
                }
            }
            System.out.println("Movimientos insertados desde JSON: " + listaMovimientoJson.size());

            entityManager.getTransaction().commit();
            System.out.println("Carga desde JSON completada correctamente.");

        } catch (RuntimeException exception) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    // ---------- Lectura JSON ----------

    private static List<CategoriaJson> leerCategoriaJson() throws Exception {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(CATEGORIA_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("No se encontró el archivo: " + CATEGORIA_PATH);
        }
        ObjectMapper objectMapper = crearMapper();
        return objectMapper.readValue(
                inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, CategoriaJson.class)
        );
    }

    private static List<ProductoJson> leerProductoJson() throws Exception {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(PRODUCTO_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("No se encontró el archivo: " + PRODUCTO_PATH);
        }
        ObjectMapper objectMapper = crearMapper();
        return objectMapper.readValue(
                inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductoJson.class)
        );
    }

    private static List<MovimientoJson> leerMovimientoJson() throws Exception {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(MOVIMIENTO_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("No se encontró el archivo: " + MOVIMIENTO_PATH);
        }
        ObjectMapper objectMapper = crearMapper();
        return objectMapper.readValue(
                inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MovimientoJson.class)
        );
    }

    private static ObjectMapper crearMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    // ---------- POJOs JSON ----------

    public static class CategoriaJson {
        private String nombre;
        private String descripcion;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }

    public static class ProductoJson {
        private String nombre;
        private String categoriaNombre;
        private Double precio;
        private Integer stockActual;
        private Boolean activo;
        private LocalDate fechaCreacion;
        private LocalDate fechaActualizacion;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCategoriaNombre() {
            return categoriaNombre;
        }

        public void setCategoriaNombre(String categoriaNombre) {
            this.categoriaNombre = categoriaNombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        public Integer getStockActual() {
            return stockActual;
        }

        public void setStockActual(Integer stockActual) {
            this.stockActual = stockActual;
        }

        public Boolean getActivo() {
            return activo;
        }

        public void setActivo(Boolean activo) {
            this.activo = activo;
        }

        public LocalDate getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(LocalDate fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }

        public LocalDate getFechaActualizacion() {
            return fechaActualizacion;
        }

        public void setFechaActualizacion(LocalDate fechaActualizacion) {
            this.fechaActualizacion = fechaActualizacion;
        }
    }

    public static class MovimientoJson {
        private TipoMovimiento tipo;
        private Integer cantidad;
        private String motivo;
        private LocalDate fecha;
        private Long productoId;

        public TipoMovimiento getTipo() {
            return tipo;
        }

        public void setTipo(TipoMovimiento tipo) {
            this.tipo = tipo;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public String getMotivo() {
            return motivo;
        }

        public void setMotivo(String motivo) {
            this.motivo = motivo;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }
    }
}