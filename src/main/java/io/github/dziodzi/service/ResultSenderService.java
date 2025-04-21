package io.github.dziodzi.service;

import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.User;
import io.github.dziodzi.entity.VerificationHistory;
import io.github.dziodzi.entity.exchange.ErrorResponse;
import io.github.dziodzi.entity.exchange.PredictionResponse;
import io.github.dziodzi.entity.exchange.ResponseWrapper;
import io.github.dziodzi.exception.NeuralNetworkException;
import io.github.dziodzi.exception.UserNotFoundException;
import io.github.dziodzi.repository.ImageRepository;
import io.github.dziodzi.repository.UserRepository;
import io.github.dziodzi.repository.VerificationHistoryRepository;
import io.github.dziodzi.tools.LogExecutionTime;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@LogExecutionTime
@Service
@RequiredArgsConstructor
@Slf4j
public class ResultSenderService {

    private final ImageRepository imageRepository;
    private final VerificationHistoryRepository verificationHistoryRepository;
    private final NeuralNetworkClient neuralNetworkClient;
    private final UserRepository userRepository;

    private final Tika tika = new Tika();

    @Value("${storage.directory:/store}")
    private String storageDirectory;

    public ResponseWrapper handleImageUpload(MultipartFile file, HttpServletRequest request) {
        try {
            String mimeType = tika.detect(file.getBytes());
            if (!mimeType.startsWith("image/")) {
                throw new IllegalArgumentException("Uploaded file is not a valid image.");
            }

            log.info("Received file: name={}, size={} bytes", file.getOriginalFilename(), file.getSize());

            String fileId = UUID.randomUUID().toString();
            File dest = getFile(file, fileId);
            file.transferTo(dest);
            log.info("Saved file to: {}", dest.getAbsolutePath());

            Image image = Image.builder()
                    .id(fileId)
                    .filepath(dest.getAbsolutePath())
                    .uploadDate(LocalDateTime.now())
                    .build();

            Image savedImage = imageRepository.save(image);
            log.info("Saved image with ID: {}, filepath: {}", savedImage.getId(), savedImage.getFilepath());

            PredictionResponse prediction = neuralNetworkClient.sendImageToPrediction(dest);

            String resultString = String.format(
                    "class_description: %s, fake_probability: %.3f",
                    prediction.getClassDescription(),
                    prediction.getFakeProbability()
            );

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            log.info("Sending prediction to user: {}", username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            VerificationHistory history = VerificationHistory.builder()
                    .id(UUID.randomUUID().toString())
                    .image(savedImage)
                    .user(user)
                    .verificationDate(LocalDateTime.now())
                    .result(resultString)
                    .build();

            verificationHistoryRepository.save(history);
            log.info("Saved verification history: {}", resultString);

            return ResponseWrapper.success(prediction);
        } catch (IllegalArgumentException | IOException | UserNotFoundException e) {
            log.error(e.getMessage());
            return ResponseWrapper.error(400, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseWrapper.error(500, e.getMessage());
        }
    }
    private File getFile(MultipartFile file, String fileId) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String safeFilename = (originalFilename != null ? originalFilename : "image.png")
                .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

        String newFilename = fileId + "_" + safeFilename;

        File dir = new File(storageDirectory);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create storage directory: " + dir.getAbsolutePath());
        }

        File dest = new File(dir, newFilename);
        return dest;
    }
}