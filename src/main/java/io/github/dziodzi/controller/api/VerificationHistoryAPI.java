package io.github.dziodzi.controller.api;

import io.github.dziodzi.entity.VerificationHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Verification History", description = "Verification history operations API")
@RequestMapping("/verification-history")
public interface VerificationHistoryAPI {

    @Operation(summary = "Add a new verification history entry")
    @PostMapping("/add")
    @ResponseBody
    ResponseEntity<VerificationHistory> addVerificationHistory(@RequestBody @Valid VerificationHistory verificationHistory);

    @Operation(summary = "Delete a verification history entry by ID")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    ResponseEntity<Void> deleteVerificationHistory(@PathVariable("id") String id);

    @Operation(summary = "Get verification history entry by ID")
    @GetMapping("/{id}")
    @ResponseBody
    ResponseEntity<VerificationHistory> getVerificationHistoryById(@PathVariable("id") String id);

    @Operation(summary = "Get all verification history entries")
    @GetMapping("/all")
    @ResponseBody
    ResponseEntity<List<VerificationHistory>> getAllVerificationHistories();

    @Operation(summary = "Search verification histories by image ID, user ID, date, or result")
    @GetMapping("/search")
    @ResponseBody
    ResponseEntity<List<VerificationHistory>> searchVerificationHistories(
            @RequestParam(required = false) String imageId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String verificationDate,
            @RequestParam(required = false) String result);
}