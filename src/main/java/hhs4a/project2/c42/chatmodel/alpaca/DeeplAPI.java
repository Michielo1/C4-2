package hhs4a.project2.c42.chatmodel.alpaca;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeeplAPI {
    //Translate text to target language
    public static String translate(String text, String targetLanguage) {
        HttpClient client = HttpClient.newHttpClient();
        String body;
        body = "text=" + URLEncoder.encode(text, StandardCharsets.UTF_8) + "&target_lang=" + URLEncoder.encode(targetLanguage, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api-free.deepl.com/v2/translate"))
                .header("Authorization", "")  // Replace "apikey" with your actual API key
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error sending the translation request.", e);
        }

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            System.out.println(responseBody);

            try {
                JSONObject json = new JSONObject(responseBody);
                JSONArray translations = json.getJSONArray("translations");
                if (translations.length() > 0) {
                    JSONObject translation = translations.getJSONObject(0);
                    String translatedText = translation.getString("text");
                    System.out.println("Translated Text: " + translatedText);
                    return translatedText;
                }

            } catch (JSONException e) {
                throw new RuntimeException("Error parsing the translation response.", e);
            }
        } else {
            throw new RuntimeException("Translation request failed. Response code: " + response.statusCode());
        }

        return null;
    }
}
