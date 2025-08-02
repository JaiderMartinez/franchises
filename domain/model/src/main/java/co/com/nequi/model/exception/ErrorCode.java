package co.com.nequi.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    F409000("F409-000", "Franchise name already exists", "409"),
    F404000("F404-000", "Franchise not found", "404");

    private final String code;
    private final String message;
    private final String statusCode;
}
