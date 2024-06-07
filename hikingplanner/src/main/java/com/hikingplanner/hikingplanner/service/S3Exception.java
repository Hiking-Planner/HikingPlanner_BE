package com.hikingplanner.hikingplanner.service;

public class S3Exception extends RuntimeException {
    private ErrorCode errorCode;

    public S3Exception(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 에러 코드에 해당하는 메시지 설정
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}


