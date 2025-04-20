package io.github.dziodzi.service;

import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.VerificationHistory;
import io.github.dziodzi.entity.User;
import io.github.dziodzi.exception.NotFoundException;
import io.github.dziodzi.repository.ImageRepository;
import io.github.dziodzi.repository.VerificationHistoryRepository;
import io.github.dziodzi.repository.UserRepository;
import io.github.dziodzi.tools.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@LogExecutionTime
public class ResultSender {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final VerificationHistoryRepository verificationHistoryRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Tika tika = new Tika();

    @Value("${storage.directory:/store}")
    private String storageDirectory;

    public void sendToFrontend(Object json) {
        log.info("Sending verification result to frontend: {}", json);
        restTemplate.postForEntity("http://localhost:3000/receive", json, String.class);
    }

    public Image handleImageUpload(MultipartFile file) throws IOException {
        String mimeType = tika.detect(file.getBytes());
        if (!mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Uploaded file is not a valid image.");
        }

        log.info("Received file: name={}, size={} bytes", file.getOriginalFilename(), file.getSize());

        String fileId = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String safeFilename = (originalFilename != null ? originalFilename : "image.png")
                .replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

        String newFilename = fileId + "_" + safeFilename;

        File dest = new File(storageDirectory, newFilename);
        if (!dest.getParentFile().exists()) {
            log.warn("Storage directory does not exist. Creating: {}", dest.getParentFile().getAbsolutePath());
            dest.getParentFile().mkdirs();
        }

        log.info("Saving file to: {}", dest.getAbsolutePath());

        file.transferTo(dest);

        Image image = Image.builder()
                .id(fileId)
                .filepath(dest.getAbsolutePath())
                .uploadDate(LocalDate.now())
                .build();

        Image savedImage = imageRepository.save(image);
        log.info("Saved image with ID: {}", savedImage.getId());

        return savedImage;
    }

    public VerificationHistory createVerificationHistoryFromJson(String resultJson, String imageId, long userId) {
        log.info("Creating verification history for image ID: {}, user ID: {}", imageId, userId);

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String result = resultJson.contains("Real") ? "Real" : "Fake";

        VerificationHistory history = VerificationHistory.builder()
                .id(UUID.randomUUID().toString())
                .image(image)
                .user(user)
                .result(result)
                .verificationDate(LocalDate.now())
                .build();

        VerificationHistory saved = verificationHistoryRepository.save(history);
        log.info("Verification result saved with ID: {}", saved.getId());

        return saved;
    }

    public void processImageAndSendResult(MultipartFile file, String resultJson, long userId) throws IOException {
        log.info("Processing uploaded image...");
        Image image = handleImageUpload(file);
        VerificationHistory history = createVerificationHistoryFromJson(resultJson, image.getId(), userId);
        sendToFrontend(history);
        log.info("Finished processing image and sending result.");
    }
}