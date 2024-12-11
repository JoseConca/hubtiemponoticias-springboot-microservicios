package jc.dev.ms.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class UbicacionService {

    private final WebClient webClient;

    public UbicacionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://api.ipstack.com").build();
    }

    public Map<String, String> obtenerUbicacionReal(String ip) {
        String apiKey = ""/*Consigue tu api key de ipstack.com*/;
        
        // Llama a la API de ipstack
        @SuppressWarnings("unchecked")
		Map<String, Object> respuesta = this.webClient
                .get()
                .uri("/{ip}?access_key={apiKey}", ip, apiKey)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // Procesa la respuesta
        Map<String, String> ubicacion = new HashMap<>();
        ubicacion.put("ciudad", (String) respuesta.get("city"));
        ubicacion.put("pais", (String) respuesta.get("country_name"));
        return ubicacion;
    }
    
    public String obtenerIpCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // Si la IP es localhost la convertimos a la ip de telde.es (ubicada en Madrid)
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = "82.98.154.85";
        }
        return ip;
    }
}
