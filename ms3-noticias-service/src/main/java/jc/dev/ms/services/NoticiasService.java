package jc.dev.ms.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class NoticiasService {

    private final WebClient webClient;

    public NoticiasService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://newsapi.org/v2").build();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> buscarNoticias(String query) {
        String apiKey = ""/*Consigue tu api key de newsapi.org*/;

        // Llama a la API
        Map<String, Object> respuesta = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/everything")
                        .queryParam("q", query)
                        .queryParam("apiKey", apiKey)
                        .queryParam("language", "es")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // Procesa la respuesta
        if (respuesta != null && respuesta.containsKey("articles")) {
            return (List<Map<String, Object>>) respuesta.get("articles");
        } else {
            return null;
        }
    }
}
