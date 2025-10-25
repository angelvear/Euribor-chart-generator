import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraficaEuribor {
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
            System.out.println("Respuesta de la API RapidAPI: " + json);

            // Extraer valores del Euribor
            String w1 = extractValue(json, "1w");
            String m1 = extractValue(json, "1m");
            String m3 = extractValue(json, "3m");
            String m6 = extractValue(json, "6m");
            String m12 = extractValue(json, "12m");

            if (w1 == null) w1 = "0";
            if (m1 == null) m1 = "0";
            if (m3 == null) m3 = "0";
            if (m6 == null) m6 = "0";
            if (m12 == null) m12 = "0";

            // Configurar QuickChart
            String chartJson = "{"
                    + "\"type\":\"line\","
                    + "\"data\":{"
                    + "\"labels\":[\"1 Mes\",\"3 Meses\",\"6 Meses\",\"12 Meses\"],"
                    + "\"datasets\":[{"
                    + "\"label\":\"Euribor (%)\","
                    + "\"data\":[" + m1 + "," + m3 + "," + m6 + "," + m12 + "],"
                    + "\"fill\":false,"
                    + "\"borderColor\":\"blue\""
                    + "}]"
                    + "},"
                    + "\"options\":{"
                    + "\"title\":{\"display\":true,\"text\":\"Tasas Euribor actuales\"}"
                    + "}"
                    + "}";

            String encoded = URLEncoder.encode(chartJson, "UTF-8");
            String chartUrl = "https://quickchart.io/chart?c=" + encoded;

            System.out.println("Gráfica del Euribor:");
            System.out.println(chartUrl);
            System.out.println("Abre esta URL en tu navegador para ver la gráfica.");

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
