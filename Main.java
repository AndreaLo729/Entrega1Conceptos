import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase principal encargada de procesar los archivos de entrada (vendedores, productos y ventas)
 * y generar los reportes ordenados en formato CSV.
 * 
 * @author Andrea Lozano Beltrán y Grupo de Trabajo
 * @version 2.0
 */
public class Main {

    // Estructuras de datos para almacenar la informacion en memoria
    private static Map<Long, String> mapaVendedores = new HashMap<>();
    private static Map<Long, Double> totalVentasVendedor = new HashMap<>();
    private static Map<Integer, String> mapaProductosNombres = new HashMap<>();
    private static Map<Integer, Double> mapaProductosPrecios = new HashMap<>();
    private static Map<Integer, Integer> mapaProductosCantidad = new HashMap<>();

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando procesamiento de datos de ventas...");

            // 1. Cargar datos base
            cargarInformacionVendedores("informacion_vendedores.txt");
            cargarInformacionProductos("informacion_productos.txt");

            // 2. Procesar archivos de ventas en el directorio actual
            procesarArchivosDeVentas();

            // 3. Generar reportes solicitados
            generarReporteVendedores("reporte_vendedores_recaudo.csv");
            generarReporteProductos("reporte_productos_cantidad.csv");

            System.out.println("Proceso finalizado exitosamente. Reportes CSV generados correctamente.");

        } catch (Exception e) {
            System.err.println("Error durante la ejecucion del programa principal: " + e.getMessage());
        }
    }

    private static void cargarInformacionVendedores(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    long id = Long.parseLong(partes[1].trim());
                    String nombreCompleto = partes[2].trim() + " " + partes[3].trim();
                    mapaVendedores.put(id, nombreCompleto);
                    totalVentasVendedor.put(id, 0.0);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar vendedores: " + e.getMessage());
        }
    }

    private static void cargarInformacionProductos(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    int idProducto = Integer.parseInt(partes[0].trim());
                    String nombreProducto = partes[1].trim();
                    String precioStr = partes[2].trim().replace(",", ".");
                    double precio = Double.parseDouble(precioStr);

                    mapaProductosNombres.put(idProducto, nombreProducto);
                    mapaProductosPrecios.put(idProducto, precio);
                    mapaProductosCantidad.put(idProducto, 0);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
    }

    private static void procesarArchivosDeVentas() {
        File directorioActual = new File(".");
        File[] archivos = directorioActual.listFiles((dir, name) -> name.startsWith("vendedor_") && name.endsWith(".txt"));

        if (archivos == null) return;

        for (File archivo : archivos) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String primeraLinea = br.readLine();
                if (primeraLinea == null) continue;

                String[] partesEncabezado = primeraLinea.split(";");
                if (partesEncabezado.length < 2) continue;
                long idVendedor = Long.parseLong(partesEncabezado[1].trim());

                String linea;
                double acumuladoVendedor = 0.0;

                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;
                    String[] partesVenta = linea.split(";");
                    if (partesVenta.length >= 2) {
                        int idProducto = Integer.parseInt(partesVenta[0].trim());
                        int cantidad = Integer.parseInt(partesVenta[1].trim());

                        // Acumular cantidad de producto
                        mapaProductosCantidad.put(idProducto, mapaProductosCantidad.getOrDefault(idProducto, 0) + cantidad);

                        // Acumular dinero del vendedor
                        double precioUnitario = mapaProductosPrecios.getOrDefault(idProducto, 0.0);
                        acumuladoVendedor += (precioUnitario * cantidad);
                    }
                }

                totalVentasVendedor.put(idVendedor, totalVentasVendedor.getOrDefault(idVendedor, 0.0) + acumuladoVendedor);

            } catch (IOException e) {
                System.err.println("Error al procesar archivo de ventas " + archivo.getName() + ": " + e.getMessage());
            }
        }
    }

    private static void generarReporteVendedores(String rutaSalida) {
        List<Map.Entry<Long, Double>> lista = new ArrayList<>(totalVentasVendedor.entrySet());
        
        // Ordenar de mayor a menor por recaudacion
        lista.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaSalida))) {
            for (Map.Entry<Long, Double> entry : lista) {
                String nombre = mapaVendedores.getOrDefault(entry.getKey(), "Vendedor Desconocido");
                bw.write(nombre + ";" + String.format("%.2f", entry.getValue()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al generar reporte de vendedores: " + e.getMessage());
        }
    }

    private static void generarReporteProductos(String rutaSalida) {
        List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(mapaProductosCantidad.entrySet());

        // Ordenar descendente por cantidad vendida
        lista.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaSalida))) {
            for (Map.Entry<Integer, Integer> entry : lista) {
                int idProd = entry.getKey();
                String nombre = mapaProductosNombres.getOrDefault(idProd, "Producto Desconocido");
                double precio = mapaProductosPrecios.getOrDefault(idProd, 0.0);

                bw.write(nombre + ";" + String.format("%.2f", precio) + ";" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al generar reporte de productos: " + e.getMessage());
        }
    }
}