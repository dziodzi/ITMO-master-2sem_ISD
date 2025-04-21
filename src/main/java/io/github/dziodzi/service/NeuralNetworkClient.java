package io.github.dziodzi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dziodzi.entity.exchange.PredictionResponse;
import io.github.dziodzi.exception.NeuralNetworkException;
import io.github.dziodzi.tools.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@LogExecutionTime
@Component
@RequiredArgsConstructor
@Slf4j
public class NeuralNetworkClient {

    @Value("${custom.address}")
    private String address;

    @Value("${custom.ports.neural-network}")
    private int neuralNetworkPort;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PredictionResponse sendImageToPrediction(File imageFile) {

        String url = address + ":" + neuralNetworkPort + "/predict";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(imageFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            log.error(response.getBody());
            throw new NeuralNetworkException("Bad Request (400) - Invalid data provided to the neural network", 400);
        } else if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new NeuralNetworkException("Forbidden (403) - Access to the neural network is denied", 403);
        } else if (response.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
            throw new NeuralNetworkException("Unprocessable Entity (422) - Invalid data or processing Exception", 422);
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            throw new NeuralNetworkException("Internal Server Exception (500) - Neural network service failed", 500);
        }

        try {
            return objectMapper.readValue(response.getBody(), PredictionResponse.class);
        } catch (Exception e) {
            log.error("Exception parsing JSON response", e);
            throw new NeuralNetworkException("Failed to parse response from neural network", response.getStatusCodeValue());
        }
    }
}