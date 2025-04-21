package io.github.dziodzi.service;

import io.github.dziodzi.entity.Image;
import io.github.dziodzi.exception.NotFoundException;
import io.github.dziodzi.repository.ImageRepository;
import io.github.dziodzi.tools.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@LogExecutionTime
public class ImageService {

    private final ImageRepository imageRepository;

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public Image getById(String id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Image not found"));
    }

    public List<Image> getByUploadDateRange(LocalDateTime from, LocalDateTime to) {
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
}