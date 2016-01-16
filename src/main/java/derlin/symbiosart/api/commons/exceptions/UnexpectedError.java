package derlin.symbiosart.api.commons.exceptions;

/**
 * @author: Lucy Linder
 * @date: 16.01.2016
 */
public class UnexpectedError extends SymbiosArtException{

    public UnexpectedError(){
    }


    public UnexpectedError( String message ){
        super( message );
    }


    public UnexpectedError( String message, Throwable cause ){
        super( message, cause );
    }


    public UnexpectedError( Throwable cause ){
        super( cause );
    }


    public UnexpectedError( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }


    public UnexpectedError( Exception e ){
        super( e );
    }
}//end class
