package derlin.symbiosart.jetty.rest;

/**
 * @author: Lucy Linder
 * @date: 26.12.2015
 */

import derlin.symbiosart.api.ApiProvider;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.commons.TagsVector;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path( "/images" )
public class ImagesService{

    private Interfaces.IIMagesApi api = new ApiProvider().getImagesApi();


    // curl -X POST -H 'Content-Type:application/json' --data '{"wedding": 2}'
    // http://localhost:8680/rest/images/suggestions/10
    @POST
    @Path( "/suggestions/{nbr}" )
    @Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public List<org.bson.Document> get( TagsVector tagsVector, @PathParam( "nbr" ) int nbr ){
        return api.getSuggestions( tagsVector, nbr );
    }


    // curl -v -X POST -H 'Accept:application/xml' http://localhost:8680/rest/images/example/tagsvector
    @POST
    @Path( "/example/tagsvector" )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public TagsVector gelt(){
        TagsVector vector = new TagsVector();
        vector.put( "newyork", 3 );
        vector.put( "wedding", 4 );
        vector.put( "sea", -1 );
        return vector;
    }
}//end class
