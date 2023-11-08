package by.vsu.pet.shared.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {
    public BadRequestException(String message) {
        super(HttpStatusCode.valueOf(400),message);
    }
}
