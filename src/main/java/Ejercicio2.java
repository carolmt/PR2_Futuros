import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


/*En el segundo ejercicio debes pedir al usuario 2 rutas. Debe comprimir la carpeta /
fichero de la primera ruta en formato ZIP. Una vez terminado de crear el archivo ZIP
debe moverlo a la segunda ruta facilitada por el usuario*/
public class Ejercicio2 {
    public static void main(String[] args) throws IOException {
        Scanner reader = new Scanner(System.in);

        System.out.println("Introduce la ruta de la carpeta/fichero que deseas comprimir: ");
        String archivoComprimir = reader.nextLine();
        System.out.println("Introduce la ruta dónde quieras guardar el archivo comprimido: ");
        String rutaComprmido = reader.nextLine();


        File fileToCompress = new File(archivoComprimir);

        if (fileToCompress.isDirectory()) {
            System.out.println("Se ha detectado un directorio.");
            ZipDirectory zipDirectory = new ZipDirectory(archivoComprimir);

            CompletableFuture.supplyAsync(() -> zipDirectory.comprimir())
                    .thenAccept(compressionResult -> {
                        if (compressionResult) {
                            System.out.println("Directorio comprimido con éxito.");
                        } else {
                            System.out.println("Error al comprimir el directorio.");
                        }
                    })
                    .exceptionally(error -> {
                        System.out.println("Error al comprimir el directorio: " + error.getMessage());
                        return null;
                    });

        } else if (fileToCompress.isFile()) {
            System.out.println("Se ha detectado un archivo.");
            ZipFile zipFile = new ZipFile(archivoComprimir);
            CompletableFuture.supplyAsync(() -> zipFile.comprimir())
                    .thenAccept(compressionResult -> {
                        if (compressionResult) {
                            System.out.println("Archivo comprimido con éxito.");
                        } else {
                            System.out.println("Error al comprimir el archivo.");
                        }
                    })
                    .exceptionally(error -> {
                        System.out.println("Error al comprimir el archivo: " + error.getMessage());
                        return null;
                    });
        }
    }
    }


