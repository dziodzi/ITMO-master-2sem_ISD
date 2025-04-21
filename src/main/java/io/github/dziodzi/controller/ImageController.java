package io.github.dziodzi.controller;

import io.github.dziodzi.controller.api.ImageAPI;
import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.exchange.ResponseWrapper;
import io.github.dziodzi.service.ImageService;
import io.github.dziodzi.service.ResultSenderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController implements ImageAPI {

    private final ImageService imageService;
    private final ResultSenderService resultSender;

    @PostMapping("/add")
    public ResponseEntity<Image> addImage(@RequestBody Image image) {
        return ResponseEntity.ok(imageService.save(image));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable String id, @RequestBody Image image) {
        imageService.updateFilepath(id, image.getFilepath());
        return ResponseEntity.ok(imageService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable String id) {
        imageService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable String id) {
        return ResponseEntity.ok(imageService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getAll());
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> imageExists(@PathVariable String id) {
        return ResponseEntity.ok(imageService.existsById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Image>> searchImages(@RequestParam(required = false) String filepath,
                                                    @RequestParam(required = false) String uploadDate) {
        if (filepath != null) {
            return ResponseEntity.ok(imageService.getByFilepath(filepath));
        } else if (uploadDate != null) {
            return ResponseEntity.ok(imageService.getByUploadDateRange(LocalDateTime.parse(uploadDate), LocalDateTime.now()));
        } else {
            return ResponseEntity.ok(imageService.getAll());
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<ResponseWrapper> handleImageUpload(@RequestParam("file") MultipartFile file,
                                                             HttpServletRequest request) {
        ResponseWrapper result = resultSender.handleImageUpload(file, request);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
