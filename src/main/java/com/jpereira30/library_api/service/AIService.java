package com.jpereira30.library_api.service;

import com.jpereira30.library_api.dto.Choice;
import com.jpereira30.library_api.dto.OpenAIResponse;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AIService {

  private final WebClient webClient;

  @Value("${openai.api.url}")
  private String apiUrl;

  @Value("${openai.api.key}")
  private String apiKey;

  @Value("${openai.model}")
  private String model;

  public AIService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }

  public Mono<String> generateInsights(String description) {
    Map<String, Object> requestPayload =
        Map.of(
            "model",
            model,
            "messages",
            List.of(
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of(
                    "role",
                    "user",
                    "content",
                    "Generate a short and engaging tagline for the following book: "
                        + description)),
            "max_tokens",
            60);

    return webClient
        .post()
        .uri(apiUrl + "/v1/chat/completions") // Updated to chat endpoint
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestPayload)
        .retrieve()
        .bodyToMono(OpenAIResponse.class)
        .map(
            response ->
                response.getChoices().stream()
                    .findFirst()
                    .map(Choice::getText)
                    .orElse("No insight available"))
        .onErrorResume(e -> Mono.just("Failed to generate insights"));
  }
}
