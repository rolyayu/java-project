package by.vsu.pet.shared.exceptions;

public class NotFoundException extends HttpException{
    public NotFoundException(String message) {
        super(message, (short) 404);
    }
}
