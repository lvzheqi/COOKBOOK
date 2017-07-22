package de.hs.inform.lyuz.cookbook.model.exception;


public class ConvertErrorException extends ErrorException{
        
    public ConvertErrorException(){
    }
    
    public ConvertErrorException(String message,String errorClass){
        super(message, errorClass);
    }

}
