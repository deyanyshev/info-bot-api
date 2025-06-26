package ru.it.vs.info_bot_api.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ApiException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public static ApiException buildApiException(HttpStatus httpStatus, String message) {
        return ApiException.builder()
                .status(httpStatus)
                .message(message)
                .build();
    }

}
