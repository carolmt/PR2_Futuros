/*con javaFx y trabajando con futuros
* En el primer ejercicio debes pedir al usuario una URL, descargar la página web en
esa URL, y una vez descargada del todo mostrar al usuario el contenido. Debes usar
futuros para sincronizar las dos tareas: descargar y mostrar. Te puede ser de ayuda
el siguiente enlace:
* https://zetcode.com/java/readwebpage/*/

import java.io.IOException;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Ejercicio1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner reader = new Scanner(System.in);

        System.out.println("Introduce una URL: ");
        String url = reader.nextLine();

        CompletableFuture<String> descargaFutura = CompletableFuture.supplyAsync(() -> {
            try {
                return descargarContenido(url);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        descargaFutura.thenAccept(contenido -> {
            System.out.println("Contenido descargado:");
            System.out.println(contenido);
        });

        // Esperar a que ambas tareas se completen
        try {
            descargaFutura.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
}
        /*

        HttpRequest solicitud = HttpRequest.newBuilder() //Construimos una solicitud sincrónica a la página web.
                .uri(URI.create(url))
                .GET() // El método predeterminado es GET.
                .build();

        HttpResponse<String> respuesta = client.send(solicitud, //Enviamos la solicitud y recuperamos el contenido de la respuesta
                HttpResponse.BodyHandlers.ofString()); // HttpResponse.BodyHandlers.ofString ya que esperamos una respuesta HTML

        System.out.println(respuesta.body());
    }
}*/
