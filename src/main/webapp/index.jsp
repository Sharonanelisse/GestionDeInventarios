<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard Inventarios</title>
</head>
<body>
<h1>Dashboard</h1>
<div>
    <p>Total productos: ${totalProductos}</p>
    <p>Productos bajo stock: ${productosBajoStock}</p>
    <p>Productos inactivos: ${productosInactivos}</p>
    <p>Total categorías: ${totalCategorias}</p>
    <p>Movimientos esta semana: ${movimientosSemana}</p>
    <p>Última actualización: ${ultimaActualizacion}</p>
</div>
<a href="productos.jsp">Gestionar Productos</a> |
<a href="categorias.jsp">Gestionar Categorías</a> |
<a href="movimientos.jsp">Movimientos</a>
</body>
</html>
