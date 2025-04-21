package io.github.dziodzi.entity.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseWrapper {
    private int statusCode;
    private String message;
    private Object data;

    public static ResponseWrapper success(Object data) {
        return new ResponseWrapper(200, "Success", data);
    }

    public static ResponseWrapper error(int statusCode, String message) {
        return new ResponseWrapper(statusCode, message, null);
    }
}