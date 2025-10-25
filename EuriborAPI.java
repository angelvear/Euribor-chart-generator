import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EuriborAPI {
    public static void main(String[] args) {
        String url = "https://euribor.p.rapidapi.com/";
        String apiKey = "483cd4c282mshf5f9f63fd2230c3p1c3030jsn0fc2004bd778";
        String host = "euribor.p.rapidapi.com";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("x-rapidapi-key", apiKey)
                    .header("x-rapidapi-host", host)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            System.out.println("Respuesta de la API RapidAPI:");
            System.out.println(json);

            // Extraer valores principales
            String w1 = extractValue(json, "1w");
            String m1 = extractValue(json, "1m");
            String m3 = extractValue(json, "3m");
            String m6 = extractValue(json, "6m");
            String m12 = extractValue(json, "12m");

            System.out.println("\nValores extraídos del Euribor:");
            System.out.println("1 Semana: " + w1);
            System.out.println("1 Mes: " + m1);
            System.out.println("3 Meses: " + m3);
            System.out.println("6 Meses: " + m6);
            System.out.println("12 Meses: " + m12);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractValue(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*([0-9]+\\.?[0-9]*)");
        Matcher m = p.matcher(json);
        if (m.find()) return m.group(1);
        return null;
    }
}
