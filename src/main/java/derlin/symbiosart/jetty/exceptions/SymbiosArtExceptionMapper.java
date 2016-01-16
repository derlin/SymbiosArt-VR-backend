package derlin.symbiosart.jetty.exceptions;

import derlin.symbiosart.api.commons.exceptions.NotFoundException;
import derlin.symbiosart.api.commons.exceptions.SymbiosArtException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author: Lucy Linder
 * @date: 16.01.2016
 */

@Provider
public class SymbiosArtExceptionMapper implements ExceptionMapper<SymbiosArtException>{


    @Override
    public Response toResponse( SymbiosArtException e ){
        int status = e instanceof NotFoundException ? 404 : 500;
        return Response.status( status ).entity( e.getMessage() ).build();

    }
}//end class
