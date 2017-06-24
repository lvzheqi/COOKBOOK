package de.hs.inform.lyuz.cookbook.model.exception;


public class SystemErrorException extends Exception{

    public SystemErrorException() {
    }
    
    public SystemErrorException(String message){
        super(message);
    }
    
}
