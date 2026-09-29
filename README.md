# Sistema de Procesamiento de Ventas y Reportes en Java

**Asignatura:** Conceptos Fundamentales de Programación  
**Versión:** 2.0 (Entrega 2)  
**Autor:** Andrea Lozano Beltrán y Grupo de Trabajo  

---

## 📋 Descripción del Proyecto

Este proyecto consiste en un sistema de procesamiento batch desarrollado en Java puro (sin dependencias externas) que automatiza la lectura, consolidación y reporte de datos de ventas de una compañía. 

El sistema consta de dos módulos principales:
1. **Generador de datos de prueba (`GenerateInfoFiles`):** Crea archivos planos pseudoaleatorios con la información básica de vendedores, catálogo de productos y registros individuales de ventas por vendedor.
2. **Procesador de ventas y generador de reportes (`Main`):** Carga los archivos planos en estructuras de datos optimizadas en memoria (`HashMap`), consolida los montos recaudados por vendedor y las cantidades vendidas por producto, y exporta reportes ordenados en formato `.csv`.

---

## 🏗️ Estructura del Proyecto

```text
Entrega2/
├── Entrega1Conceptos/
│   ├── Main.java               # Programa principal de procesamiento y reportes
│   └── GenerateInfoFiles.java  # Módulo de generación de archivos de prueba
└── README.md                   # Guía de ejecución y explicación del proyecto
```

---

## 🔄 Arquitectura y Flujo de Procesamiento

```text
┌─────────────────────────────────────────────────────────────┐
│                    1. Generación de Datos                   │
│                     (GenerateInfoFiles)                     │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   2. Archivos Planos (.txt)                 │
│  - informacion_vendedores.txt                               │
│  - informacion_productos.txt                                │
│  - vendedor_<ID>.txt                                        │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│               3. Procesamiento y Consolidación              │
│                           (Main)                            │
│  - Lectura en memoria (HashMap<Key, Value>)                 │
│  - Ordenamiento descendente (Collections / List.sort)       │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                    4. Reportes de Salida (.csv)             │
│  - reporte_vendedores_recaudo.csv                           │
│  - reporte_productos_cantidad.csv                           │
└─────────────────────────────────────────────────────────────┘
```

---

## ⚙️ Explicación Técnica de los Componentes

### 1. `GenerateInfoFiles.java`
Esta clase es responsable de simular información de ventas para testing. Contiene los siguientes métodos principales:
- **`createSalesManInfoFile(int salesmanCount)`**: Genera el archivo `informacion_vendedores.txt` con cédula, nombres y apellidos generados aleatoriamente.
- **`createProductsFile(int productsCount)`**: Genera el archivo `informacion_productos.txt` con nombres de productos y precios aleatorios redondeados a 2 decimales.
- **`createSalesManFile(int randomSalesCount, String name, long id)`**: Genera archivos de ventas nombrados `vendedor_<id>.txt` donde la primera línea es el documento del vendedor y las líneas siguientes detallan `IDProducto;CantidadProductoVendido`.

### 2. `Main.java`
Es el núcleo de la aplicación. Ejecuta la lógica en 3 fases secuenciales:
1. **`cargarInformacionVendedores` y `cargarInformacionProductos`:** Lee los archivos base de vendedores y productos construyendo tablas de búsqueda rápida (`HashMap`).
2. **`procesarArchivosDeVentas`:** Escanea el directorio actual buscando archivos que inicien con el prefijo `vendedor_` y terminen en `.txt`. Procesa cada registro de venta acumulando el total en dinero para el vendedor y las unidades totales vendidas por producto.
3. **`generarReporteVendedores` y `generarReporteProductos`:** Convierte los datos acumulados en listas, los ordena de mayor a menor y los escribe en los archivos `.csv` correspondientes.

---

## 📄 Especificación de Formatos de Archivo

Todos los archivos emplean el carácter punto y coma (`;`) como delimitador.

### Archivos de Entrada (`.txt`)

#### 1. `informacion_vendedores.txt`
Formato: `TipoDocumento;NumeroDocumento;NombresVendedor;ApellidosVendedor`  
Ejemplo:
```text
CC;1019116171;Andrea;Lozano
CE;1020304050;Jhonatan;Rodriguez
```

#### 2. `informacion_productos.txt`
Formato: `IDProducto;NombreProducto;PrecioPorUnidadProducto`  
Ejemplo:
```text
1;Laptop 1;125000.50
2;Mouse 2;45000.00
```

#### 3. `vendedor_<IDDocumento>.txt`
Línea 1 (Encabezado): `TipoDocumento;NumeroDocumento`  
Líneas 2 en adelante: `IDProducto;CantidadProductoVendido;`  
Ejemplo:
```text
CC;1019116171
1;3;
2;5;
```

---

### Archivos de Salida (`.csv`)

#### 1. `reporte_vendedores_recaudo.csv`
Ordenado de mayor a menor por total recaudado.  
Formato: `NombreCompletoVendedor;TotalRecaudado`  
Ejemplo:
```text
Andrea Lozano;599999.00
Diego Restrepo;350000.00
```

#### 2. `reporte_productos_cantidad.csv`
Ordenado de mayor a menor por cantidad total vendida.  
Formato: `NombreProducto;PrecioPorUnidad;CantidadTotalVendida`  
Ejemplo:
```text
Laptop 1;125000.50;15
Mouse 2;45000.00;8
```

---

## 🚀 Guía de Compilación y Ejecución Paso a Paso

### Prerrequisitos
- Tener instalado el JDK de Java (versión 8 o superior).
- Acceso a la consola de comandos (Terminal en Linux/Mac o Command Prompt / PowerShell en Windows).

---

### Paso 1: Ubicarse en el directorio del código fuente
Abre la consola y navega hasta la carpeta `Entrega1Conceptos`:

```bash
cd Entrega1Conceptos
```

---

### Paso 2: Compilar los archivos Java
Compila ambas clases con el comando `javac`:

```bash
javac GenerateInfoFiles.java Main.java
```
*Nota: Si la compilación es exitosa, se generarán los archivos `.class` correspondientes.*

---

### Paso 3: Generar archivos de datos de prueba
Ejecuta la clase `GenerateInfoFiles` para crear los archivos `.txt` aleatorios:

```bash
java GenerateInfoFiles
```
**Resultado esperado:**
```text
Proceso finalizado exitosamente. Archivos de prueba generados correctamente.
```
Se crearán en la carpeta los archivos:
- `informacion_vendedores.txt`
- `informacion_productos.txt`
- `vendedor_1019116171.txt`
- `vendedor_1020304050.txt`
- `vendedor_1030405060.txt`

---

### Paso 4: Ejecutar el procesador de ventas y generar reportes
Ejecuta la clase principal `Main`:

```bash
java Main
```
**Resultado esperado:**
```text
Iniciando procesamiento de datos de ventas...
Proceso finalizado exitosamente. Reportes CSV generados correctamente.
```
Se crearán en la carpeta los archivos de salida:
- `reporte_vendedores_recaudo.csv`
- `reporte_productos_cantidad.csv`

---

## ✅ Verificación de Resultados

Puedes inspeccionar los reportes generados utilizando el comando `cat` (Linux/Mac) o `type` (Windows):

```bash
cat reporte_vendedores_recaudo.csv
cat reporte_productos_cantidad.csv
```

---

## 👥 Créditos
- **Autores:** Andrea Lozano Beltrán y Grupo de Trabajo
- **Institución:** Politécnico Grancolombiano / Institución Universitaria
- **Asignatura:** Conceptos Fundamentales de Programación
