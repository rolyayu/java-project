package by.vsu.pet.shared.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public abstract class HttpException extends RuntimeException{
    short status;

    LocalDateTime timestamp;

    public HttpException(String message, short status) {
        super(message);
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
