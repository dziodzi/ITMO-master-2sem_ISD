package io.github.dziodzi.repository;

import io.github.dziodzi.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    Optional<Image> findByFilepath(String filepath);

    List<Image> findByUploadDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Image> findByFilepathContainingIgnoreCase(String partialPath);
}