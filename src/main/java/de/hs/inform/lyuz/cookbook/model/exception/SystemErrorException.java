package de.hs.inform.lyuz.cookbook.model.exception;


public class SystemErrorException extends ErrorException{

    
    public SystemErrorException() {
    }
    
    public SystemErrorException(String message, String errorClass){
        super(message, errorClass);
    }
    
}
