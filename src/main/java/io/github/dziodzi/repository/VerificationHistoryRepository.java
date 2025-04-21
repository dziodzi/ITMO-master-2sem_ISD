package io.github.dziodzi.repository;

import io.github.dziodzi.entity.VerificationHistory;
import io.github.dziodzi.entity.Image;
import io.github.dziodzi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerificationHistoryRepository extends JpaRepository<VerificationHistory, String> {

    List<VerificationHistory> findByUser(User user);

    List<VerificationHistory> findByImage(Image image);

    List<VerificationHistory> findByVerificationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<VerificationHistory> findByResult(String result);

    List<VerificationHistory> findByUserAndVerificationDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
}