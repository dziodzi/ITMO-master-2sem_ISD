package io.github.dziodzi.exception;

import lombok.Getter;

@Getter
public class NeuralNetworkException extends RuntimeException {
    private final int statusCode;

    public NeuralNetworkException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}