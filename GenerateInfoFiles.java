import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase encargada de la generacion de archivos planos con datos pseudoaleatorios
 * para pruebas del sistema de ventas.
 * 
 * @author Andrea Lozano Beltran
 * @version 1.0
 */
public class GenerateInfoFiles {

    private static final String[] TIPOS_DOC = {"CC", "CE", "NIT"};
    private static final String[] NOMBRES = {"Andrea", "Jhonatan", "Diego", "Maria", "Carlos", "Laura", "Pedro", "Sofia"};
    private static final String[] APELLIDOS = {"Lozano", "Rodriguez", "Gomez", "Martinez", "Perez", "Hernandez", "Lopez"};
    private static final String[] PRODUCTOS_NOM = {"Laptop", "Mouse", "Teclado", "Monitor", "Diadema", "Impresora", "USB"};

    public static void main(String[] args) {
        try {
            // Generar archivo de información de vendedores (ejemplo: 5 vendedores)
            createSalesManInfoFile(5);

            // Generar archivo de información de productos (ejemplo: 10 productos)
            createProductsFile(10);

            // Generar archivos de ventas aleatorios para 3 vendedores de prueba
            createSalesManFile(5, "Andrea Lozano", 1019116171L);
            createSalesManFile(8, "Jhonatan Martinez", 1020304050L);
            createSalesManFile(4, "Diego Restrepo", 1030405060L);

            System.out.println("Proceso finalizado exitosamente. Archivos de prueba generados correctamente.");
        } catch (Exception e) {
            System.err.println("Error durante la generacion de archivos: " + e.getMessage());
        }
    }

    /**
     * Crea un archivo plano de ventas pseudoaleatorio para un vendedor especifico.
     * 
     * @param randomSalesCount Cantidad de ventas a generar.
     * @param name Nombre del vendedor.
     * @param id Numero de documento del vendedor.
     */
    public static void createSalesManFile(int randomSalesCount, String name, long id) {
        String fileName = "vendedor_" + id + ".txt";
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Primera linea: TipoDocumento;NumeroDocumento
            writer.write("CC;" + id);
            writer.newLine();

            // Lineas de ventas: IDProducto;CantidadProductoVendido
            for (int i = 0; i < randomSalesCount; i++) {
                int idProducto = random.nextInt(10) + 1; // ID entre 1 y 10
                int cantidadVendida = random.nextInt(5) + 1; // Cantidad entre 1 y 5
                writer.write(idProducto + ";" + cantidadVendida + ";");
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al crear archivo de ventas para " + name + ": " + e.getMessage());
        }
    }

    /**
     * Crea un archivo plano con la informacion pseudoaleatoria de productos.
     * 
     * @param productsCount Cantidad de productos a generar.
     */
    public static void createProductsFile(int productsCount) {
        String fileName = "informacion_productos.txt";
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 1; i <= productsCount; i++) {
                String nombreProducto = PRODUCTOS_NOM[random.nextInt(PRODUCTOS_NOM.length)] + " " + i;
                double precioPorUnidad = 10000 + (150000 - 10000) * random.nextDouble();
                precioPorUnidad = Math.round(precioPorUnidad * 100.0) / 100.0; // 2 decimales

                // IDProducto;NombreProducto;PrecioPorUnidadProducto
                writer.write(i + ";" + nombreProducto + ";" + String.format("%.2f", precioPorUnidad));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al crear archivo de productos: " + e.getMessage());
        }
    }

    /**
     * Crea un archivo plano con la informacion pseudoaleatoria de vendedores.
     * 
     * @param salesmanCount Cantidad de vendedores a generar.
     */
    public static void createSalesManInfoFile(int salesmanCount) {
        String fileName = "informacion_vendedores.txt";
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDoc = TIPOS_DOC[random.nextInt(TIPOS_DOC.length)];
                long numDoc = 1000000000L + (long)(random.nextDouble() * 9000000000L);
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];

                // TipoDocumento;NumeroDocumento;NombresVendedor;ApellidosVendedor
                writer.write(tipoDoc + ";" + numDoc + ";" + nombre + ";" + apellido);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al crear archivo de vendedores: " + e.getMessage());
        }
    }
}