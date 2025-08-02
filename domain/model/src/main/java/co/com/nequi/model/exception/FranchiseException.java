package co.com.nequi.model.exception;

import lombok.Getter;

@Getter
public class FranchiseException extends RuntimeException {

    private final ErrorCode errorCode;

    public FranchiseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
