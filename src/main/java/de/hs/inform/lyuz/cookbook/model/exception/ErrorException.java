package de.hs.inform.lyuz.cookbook.model.exception;

public class ErrorException extends Exception {

    private String errorClass;

    public ErrorException() {
    }

    public ErrorException(String message, String errorClass) {
        super(message);
        this.errorClass = errorClass;
    }

    public String getErrorClass() {
        return errorClass;
    }

    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

}
