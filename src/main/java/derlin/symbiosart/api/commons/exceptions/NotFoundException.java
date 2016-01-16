package derlin.symbiosart.api.commons.exceptions;

/**
 * @author: Lucy Linder
 * @date: 16.01.2016
 */
public class NotFoundException extends SymbiosArtException{

    public NotFoundException( String id ){
        super( "ID '" + id + "' not found." );
    }


}//end class
