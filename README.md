# Gestion de Investarios

Sistema de gestión de investarios con JPQL y Criteria API

## Autores
- Sharon Anelisse Marroquín Hernandez
- Eddy Alexander Cheguen Garcia
- Araceli de los Angeles Asencio y Asencio
- David Roberto Escobar Mérida
- Ismael Alejandro Liquez Muñoz
- Josimar Brandon Andrée Hernández Calzadia


## Estructura del Proyecto

```
src/main/java/com/smarroquin/gestiondeinventarios/
├── controllers/          # Controladores de la Web
├── models/               # Modelos de Categorio, producto y movimiento
├── services/             # Servicios del Inventario
├── repositories/         # Acceso a datos
├── controllers/          # Enumeraciones
└── utils/                # Utilidades
```

### Base de Datos
```bash
docker run --name postgres-jpql -e POSTGRES_PASSWORD=admin123 -e POSTGRES_USER=postgres -e POSTGRES_DB=gestionInventarios -p 5433:5432 -d postgres
```
