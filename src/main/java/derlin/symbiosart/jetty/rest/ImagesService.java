package derlin.symbiosart.jetty.rest;

import derlin.symbiosart.api.ApiProvider;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.commons.TagsVector;
import derlin.symbiosart.api.commons.exceptions.SymbiosArtException;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The REST front-end for the images API.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
@Path( "/images" )
public class ImagesService{

    private Interfaces.IIMagesApi api = new ApiProvider().getImagesApi();


    // curl -X POST -H 'Content-Type:application/json' --data '{"wedding": 2}'
    // http://localhost:8680/rest/images/suggestions/10
    @POST
    @Path( "/suggestions/{nbr}" )
    @Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public List<Document> getSuggestions( TagsVector tagsVector,
                                          @PathParam( "nbr" ) int nbr ) throws SymbiosArtException{

        return api.getSuggestions( tagsVector, nbr );

    }


    // curl -v -X POST -H 'Accept:application/xml' http://localhost:8680/rest/images/example/tagsvector
    @GET
    @Path( "/{id}" )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public Document getDetails( @PathParam( "id" ) String id ) throws SymbiosArtException{
        return api.getDetails( id );
    }
}//end class
