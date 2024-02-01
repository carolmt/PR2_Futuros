import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*En el tercer ejercicio debes descargar 10 páginas web (ponlas en un listado
directamente en el código fuente) y una vez estén descargadas las 10 páginas web
comprimirlas en un fichero .ZIP.*/
public class Ejercicio3 {
    public static void main(String[] args) {
        List<String> urls = new ArrayList<>();
        urls.add("https://www.google.com/");
        urls.add("https://www.youtube.com/");
        urls.add("https://www.facebook.com/");
        urls.add("https://www.wikipedia.org/");
        urls.add("https://www.spotify.com/es/");
        urls.add("https://www.twitter.com/");
        urls.add("https://www.amazon.es/");
        urls.add("https://www.instagram.com/");
        urls.add("https://www.reddit.com/");
        urls.add("https://www.ebay.es/");

        List<CompletableFuture<String>> descargasFuturas = new ArrayList<>();
        String rutaComprimido = ("C:\\Users\\carol\\Documents\\DAM\\PSP\\T4\\contenidoUrls.txt");

        for (String url : urls) {
            descargasFuturas.add(
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            return descargarContenido(url);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
            );
        }

        CompletableFuture<Void> escrituraCompleta = CompletableFuture.allOf(descargasFuturas.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    try (FileWriter fichero = new FileWriter(rutaComprimido)) {
                        PrintWriter pw = new PrintWriter(fichero);

                        for (CompletableFuture<String> descargaFutura : descargasFuturas) {
                            pw.println(descargaFutura.get());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        escrituraCompleta.thenAccept((voidResult) -> {
            try {
                comprimir(rutaComprimido);
                System.out.println("Contenido descargado, escrito en el archivo y comprimido.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        escrituraCompleta.join(); // Esperar a que la escritura esté completa antes de imprimir el mensaje.
    }

    private static String descargarContenido(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> respuesta = client.send(solicitud, HttpResponse.BodyHandlers.ofString());

        return respuesta.body();
    }

    private static boolean comprimir(String ruta) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ruta + ".zip");
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            ZipEntry zipEntry = new ZipEntry("contenidoUrls.txt");
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = ruta.getBytes();
            zipOut.write(bytes, 0, bytes.length);

            zipOut.closeEntry();
        }
        return true;
    }
}