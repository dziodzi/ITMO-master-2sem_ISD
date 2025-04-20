package io.github.dziodzi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "filepath", nullable = false)
    private String filepath;

    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;
}