package dev.olysykh.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "no code"),
    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "new password does not match"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "account is locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "account is disabled"),
    ACCOUNT_BAD_CREDENTIALS(302, HttpStatus.FORBIDDEN, "login / password is incorrect");

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;


}
