package io.github.dziodzi.entity.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PredictionResponse {
    @JsonProperty("class")
    private int predictedClass;

    @JsonProperty("class_description")
    private String classDescription;

    @JsonProperty("fake_probability")
    private double fakeProbability;

    @JsonProperty("image_name")
    private String imageName;
}