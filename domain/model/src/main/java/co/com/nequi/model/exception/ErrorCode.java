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
    B404000("B404-000", "Branch not found", 404),
    B409000("B409-000", "Branch name already exists", 409),
    P400000("P400-000", "Product name is required", 400),
    P404000("P404-000", "Product not found", 404),
    P409000("P409-000", "Product name already exists", 409);

    private final String code;
    private final String message;
    private final int statusCode;
}
