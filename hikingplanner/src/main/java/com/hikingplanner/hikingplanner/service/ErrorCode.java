package com.hikingplanner.hikingplanner.service;

public enum ErrorCode {
    EMPTY_FILE_EXCEPTION("파일이 비어있습니다."),
    NO_FILE_EXTENTION("파일 확장자가 없습니다."),
    INVALID_FILE_EXTENTION("유효하지 않은 파일 확장자입니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 업로드 중 I/O 오류가 발생했습니다."),
    PUT_OBJECT_EXCEPTION("S3에 객체를 업로드하는 도중 오류가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE("이미지 삭제 중 I/O 오류가 발생했습니다.");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
