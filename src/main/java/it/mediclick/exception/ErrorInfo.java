package it.mediclick.exception;

import java.time.LocalDateTime;

public class ErrorInfo 
{
    private String messaggio;
    private String codiceErrore;
    private LocalDateTime timestamp;
    

    public ErrorInfo() 
    {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorInfo(ErroreCustom errore) 
    {
        this();
        if (errore != null) 
        {
            this.messaggio = errore.getMessage();
            this.codiceErrore = errore.getErrorCode();
        }
    }
    
    
    public ErrorInfo(String messaggio,String codiceErrored) 
    {
        this();
        this.messaggio = messaggio;
        this.codiceErrore = codiceErrored;
        
    }

    public String getMessaggio() 
    {
        return messaggio;
    }

    public void setMessaggio(String messaggio) 
    {
        this.messaggio = messaggio;
    }

    public String getCodiceErrore() 
    {
        return codiceErrore;
    }

    public void setCodiceErrore(String codiceErrore) 
    {
        this.codiceErrore = codiceErrore;
    }

    public LocalDateTime getTimestamp() 
    {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) 
    {
        this.timestamp = timestamp;
    }

   
}
