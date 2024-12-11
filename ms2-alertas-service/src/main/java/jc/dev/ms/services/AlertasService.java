package jc.dev.ms.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertasService {

    private final WebClient webClient;

    public AlertasService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openweathermap.org/data/2.5").build();
    }

    @SuppressWarnings("unchecked")
	public Map<String, String> obtenerAlertas(String ciudad) {
        String apiKey = ""/*Consigue tu api key de openweathermap.org*/;


        // Llama a la API
        Map<String, Object> respuesta = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/weather")
                        .queryParam("q", ciudad)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "es")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        
        // Procesa la respuesta
        Map<String, String> alertas = new HashMap<>();
        
        // Verifica si la respuesta contiene el campo "weather" y "main"
        if (respuesta != null && respuesta.containsKey("weather") && respuesta.containsKey("main")) {
        	List<Map<String, Object>> weatherList = (List<Map<String, Object>>) respuesta.get("weather");

            Map<String, Object> weatherData = weatherList.get(0);
            Map<String, Object> mainData = (Map<String, Object>) respuesta.get("main");

            // Obtener la descripción del clima y la temperatura
            alertas.put("descripcion", (String) weatherData.get("description"));
            alertas.put("temperatura", String.valueOf(mainData.get("temp")));
        }
        return alertas;
    }
}
