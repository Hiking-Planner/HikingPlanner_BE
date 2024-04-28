package com.hikingplanner.hikingplanner.common;

public interface ResponseMessage {
    String SUCCESS= "Success";

    String VALADATION_FAIL="Valadation failed";
    String DUPLICATE_ID = "Duplicate Id";

    String SIGN_IN_FAIL = "Login information mismatch";
    String CERTIFICATION_FAIL = "Certification failed.";

    String MAIL_FAIL="Mail send failed";
    String DATABASE_ERROR = "Database error";
}
