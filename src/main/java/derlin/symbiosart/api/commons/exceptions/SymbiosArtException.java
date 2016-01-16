package derlin.symbiosart.api.commons.exceptions;

/**
 * @author: Lucy Linder
 * @date: 16.01.2016
 */
public abstract class SymbiosArtException extends RuntimeException{
    public SymbiosArtException(){
    }


    public SymbiosArtException( String message ){
        super( message );
    }


    public SymbiosArtException( String message, Throwable cause ){
        super( message, cause );
    }


    public SymbiosArtException( Throwable cause ){
        super( cause );
    }


    public SymbiosArtException( String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }
}//end class
