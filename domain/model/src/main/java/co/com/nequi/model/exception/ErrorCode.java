package co.com.nequi.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    F409000("F409-000", "Franchise name already exists", 409),
    F404000("F404-000", "Franchise not found", 404),
    F400000("F400-000", "Franchise name is required", 400),
    B400000("B400-000", "Branch name is required", 400),
    B404000("B404-000", "Franchise not found", 404),
    B409000("B409-000", "Franchise name already exists", 409);

    private final String code;
    private final String message;
    private final int statusCode;
}
