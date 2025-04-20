package io.github.dziodzi.service;

import io.github.dziodzi.entity.Image;
import io.github.dziodzi.exception.NotFoundException;
import io.github.dziodzi.repository.ImageRepository;
import io.github.dziodzi.tools.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@LogExecutionTime
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${storage.directory:/store}")
    private String storageDirectory;

    private final Tika tika = new Tika();

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public Image getById(String id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }

    public List<Image> getByUploadDateRange(LocalDate from, LocalDate to) {
        return imageRepository.findByUploadDateBetween(from, to);
    }

    public List<Image> getByFilepath(String filepath) {
        return imageRepository.findByFilepathContainingIgnoreCase(filepath);
    }

    public List<Image> getByExactFilepath(String filepath) {
        return imageRepository.findByFilepath(filepath).map(List::of).orElse(List.of());
    }

    public List<Image> getAll() {
        return imageRepository.findAll();
    }

    public boolean existsById(String id) {
        return imageRepository.existsById(id);
    }

    public void deleteById(String id) {
        imageRepository.deleteById(id);
    }

    public void updateFilepath(String id, String newPath) {
        Image image = getById(id);
        image.setFilepath(newPath);
        imageRepository.save(image);
    }

    public Image handleImageUpload(MultipartFile file) throws IOException {
        String mimeType = tika.detect(file.getBytes());
        if (!mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Uploaded file is not a valid image.");
        }

        String fileId = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String newFilename = fileId + "_" + (originalFilename != null ? originalFilename : "image.png");

        File dest = new File(storageDirectory, newFilename);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);

        Image image = Image.builder()
                .id(fileId)
                .filepath(dest.getAbsolutePath())
                .uploadDate(LocalDate.now())
                .build();

        return imageRepository.save(image);
    }
}