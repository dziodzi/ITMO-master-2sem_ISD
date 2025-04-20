package io.github.dziodzi.service;

import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.User;
import io.github.dziodzi.entity.VerificationHistory;
import io.github.dziodzi.exception.NotFoundException;
import io.github.dziodzi.repository.VerificationHistoryRepository;
import io.github.dziodzi.tools.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@LogExecutionTime
public class VerificationHistoryService {

    private final VerificationHistoryRepository historyRepository;

    public VerificationHistory save(VerificationHistory history) {
        return historyRepository.save(history);
    }

    public VerificationHistory getById(String id) {
        return historyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("VerificationHistory not found"));
    }

    public boolean existsById(String id) {
        return historyRepository.existsById(id);
    }

    public void deleteById(String id) {
        historyRepository.deleteById(id);
    }

    public List<VerificationHistory> findByUser(User user) {
        return historyRepository.findByUser(user);
    }

    public List<VerificationHistory> findByImage(Image image) {
        return historyRepository.findByImage(image);
    }

    public List<VerificationHistory> findByDateRange(LocalDate from, LocalDate to) {
        return historyRepository.findByVerificationDateBetween(from, to);
    }

    public List<VerificationHistory> findByResult(String result) {
        return historyRepository.findByResult(result);
    }

    // TODO получаем от модельки ответ, записываем и кидаем дальше
    public VerificationHistory createFromJson(String resultJson, Image image, User user) {
        String id = UUID.randomUUID().toString();
        String result = resultJson.contains("Real") ? "Real" : "Fake";
        VerificationHistory history = VerificationHistory.builder()
                .id(id)
                .image(image)
                .user(user)
                .result(result)
                .verificationDate(LocalDate.now())
                .build();
        return save(history);
    }
}