package it.mediclick.exception;

public class RecensioneException extends Exception implements ErroreCustom
{

    private static final long serialVersionUID = 1L;
    
    private final String errorCode;

    public RecensioneException(String message, String errorCode) 
    {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() 
    {
        return errorCode;
    }

}
