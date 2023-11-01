package by.vsu.pet.shared.exceptions;

public class BadRequestException extends HttpException{
    public BadRequestException(String message) {
        super(message, (short) 400);
    }
}
