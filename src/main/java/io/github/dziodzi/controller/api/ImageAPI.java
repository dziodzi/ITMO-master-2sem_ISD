package io.github.dziodzi.controller.api;

import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.VerificationHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Image", description = "Image operations API")
@RequestMapping("/images")
public interface ImageAPI {

    @Operation(summary = "Add a new image")
    @PostMapping("/add")
    @ResponseBody
    ResponseEntity<Image> addImage(@RequestBody @Valid Image image);

    @Operation(summary = "Update an image's filepath by ID")
    @PutMapping("/update/{id}")
    @ResponseBody
    ResponseEntity<Image> updateImage(@PathVariable("id") String id, @RequestBody @Valid Image image);

    @Operation(summary = "Delete an image by ID")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    ResponseEntity<Void> deleteImage(@PathVariable("id") String id);

    @Operation(summary = "Get image by ID")
    @GetMapping("/{id}")
    @ResponseBody
    ResponseEntity<Image> getImageById(@PathVariable("id") String id);

    @Operation(summary = "Get all images")
    @GetMapping("/all")
    @ResponseBody
    ResponseEntity<List<Image>> getAllImages();

    @Operation(summary = "Check if an image exists by ID")
    @GetMapping("/exists/{id}")
    @ResponseBody
    ResponseEntity<Boolean> imageExists(@PathVariable("id") String id);

    @Operation(summary = "Search images by file path or upload date")
    @GetMapping("/search")
    @ResponseBody
    ResponseEntity<List<Image>> searchImages(@RequestParam(required = false) String filepath,
                                             @RequestParam(required = false) String uploadDate);

    @PostMapping("/upload")
    ResponseEntity<?> handleImageUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request);
}