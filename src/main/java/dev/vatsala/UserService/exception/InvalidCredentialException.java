package dev.vatsala.UserService.exception;

public class InvalidCredentialException extends RuntimeException{

    public InvalidCredentialException(String s)
    {
        super(s);
    }
}
