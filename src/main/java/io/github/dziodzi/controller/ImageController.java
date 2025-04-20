package io.github.dziodzi.controller;

import io.github.dziodzi.controller.api.ImageAPI;
import io.github.dziodzi.entity.Image;
import io.github.dziodzi.service.ImageService;
import io.github.dziodzi.service.ResultSender;
import io.github.dziodzi.tools.LogExecutionTime;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@LogExecutionTime
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController implements ImageAPI {

    private final ImageService imageService;
    private final ResultSender resultSender;

    @Override
    @PostMapping("/add")
    public ResponseEntity<Image> addImage(@RequestBody @Valid Image image) {
        return ResponseEntity.ok(imageService.save(image));
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable("id") String id, @RequestBody @Valid Image image) {
        imageService.updateFilepath(id, image.getFilepath());
        return ResponseEntity.ok(imageService.getById(id));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") String id) {
        imageService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.getById(id));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getAll());
    }

    @Override
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> imageExists(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.existsById(id));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<Image>> searchImages(@RequestParam(required = false) String filepath,
                                                    @RequestParam(required = false) String uploadDate) {
        if (filepath != null) {
            return ResponseEntity.ok(imageService.getByFilepath(filepath));
        } else if (uploadDate != null) {
            return ResponseEntity.ok(imageService.getByUploadDateRange(LocalDate.parse(uploadDate), LocalDate.now()));
        } else {
            return ResponseEntity.ok(imageService.getAll());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleImageUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            resultSender.handleImageUpload(file);
            return ResponseEntity.ok("Image uploaded successfully and result sent.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }
}