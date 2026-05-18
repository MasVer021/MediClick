package it.mediclick.exception;

public class AuthException extends Exception implements ErroreCustom
{

	private static final long serialVersionUID = 1L;

	private final String errorCode;

	public AuthException(String message, String errorCode)
	{
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

}
