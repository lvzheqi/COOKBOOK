package de.hs.inform.lyuz.cookbook.model.exception;


public class ParserErrorException extends ErrorException{
    
    
    public ParserErrorException(){
    }

    public ParserErrorException(String message, String errorClass) {
        super(message, errorClass);
    }
}
