package io.github.dziodzi.controller;

import io.github.dziodzi.controller.api.VerificationHistoryAPI;
import io.github.dziodzi.entity.VerificationHistory;
import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.User;
import io.github.dziodzi.service.VerificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verification-history")
public class VerificationHistoryController implements VerificationHistoryAPI {

    private final VerificationHistoryService verificationHistoryService;

    @Override
    @PostMapping("/add")
    public ResponseEntity<VerificationHistory> addVerificationHistory(@RequestBody @Valid VerificationHistory verificationHistory) {
        return ResponseEntity.ok(verificationHistoryService.save(verificationHistory));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVerificationHistory(@PathVariable("id") String id) {
        verificationHistoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<VerificationHistory> getVerificationHistoryById(@PathVariable("id") String id) {
        return ResponseEntity.ok(verificationHistoryService.getById(id));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<VerificationHistory>> getAllVerificationHistories() {
        return ResponseEntity.ok(verificationHistoryService.findByDateRange(LocalDateTime.MIN, LocalDateTime.MAX));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<VerificationHistory>> searchVerificationHistories(
            @RequestParam(required = false) String imageId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String verificationDate,
            @RequestParam(required = false) String result) {

        List<VerificationHistory> histories = verificationHistoryService.findByDateRange(
                verificationDate != null ? LocalDateTime.parse(verificationDate) : LocalDateTime.MIN,
                LocalDateTime.now()
        );

        if (imageId != null) {
            Image image = new Image();
            histories = verificationHistoryService.findByImage(image);
        }

        if (userId != null) {
            User user = new User();
            histories = verificationHistoryService.findByUser(user);
        }

        if (result != null) {
            histories = verificationHistoryService.findByResult(result);
        }

        return ResponseEntity.ok(histories);
    }
}