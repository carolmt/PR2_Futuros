import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Ejercicio2 {
    public static void main(String[] args) throws IOException {
        Scanner reader = new Scanner(System.in);

        System.out.println("Introduce la ruta de la carpeta/fichero que deseas comprimir: ");
        String archivoComprimir = reader.nextLine();
        File from = new File (archivoComprimir);

        System.out.println("Introduce la ruta dónde quieras guardar el archivo comprimido: ");
        String rutaComprmido = reader.nextLine();
        File to = new File (rutaComprmido);


        File fileToCompress = new File(archivoComprimir);

        if (fileToCompress.isDirectory()) {
            System.out.println("Se ha detectado un directorio.");
            ZipDirectory zipDirectory = new ZipDirectory(archivoComprimir);

            CompletableFuture.supplyAsync(() -> zipDirectory.comprimir(archivoComprimir))
                    .thenAccept(compressionResult -> {
                        if (compressionResult) {
                            System.out.println("Directorio comprimido con éxito.");
                            try {
                                Files.move(Path.of(archivoComprimir + ".zip"), Path.of(rutaComprmido, from.getName() + ".zip"), StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("Archivo movido con éxito.");
                            } catch (IOException e) {
                                System.out.println("Error al mover el archivo: " + e.getMessage());
                            }
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
            CompletableFuture.supplyAsync(() -> {
                        try {
                            return zipFile.comprimir(archivoComprimir);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenAccept(compressionResult -> {
                        if (compressionResult) {
                            System.out.println("Archivo comprimido con éxito.");
                            try {
                                Files.move(Path.of(archivoComprimir + ".zip"), Path.of(rutaComprmido + "/" + from.getName() + ".zip"), StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("Archivo movido con éxito.");
                            } catch (IOException e) {
                                System.out.println("Error al mover el archivo: " + e.getMessage());
                            }
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


