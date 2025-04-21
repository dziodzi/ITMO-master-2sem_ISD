package io.github.dziodzi.service;

import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.User;
import io.github.dziodzi.entity.VerificationHistory;
import io.github.dziodzi.exception.NotFoundException;
import io.github.dziodzi.repository.VerificationHistoryRepository;
import io.github.dziodzi.tools.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<VerificationHistory> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return historyRepository.findByVerificationDateBetween(from, to);
    }

    public List<VerificationHistory> findByResult(String result) {
        return historyRepository.findByResult(result);
    }
}