package derlin.symbiosart.jetty.rest;

/**
 * @author: Lucy Linder
 * @date: 26.12.2015
 */

import derlin.symbiosart.query.MirflickrQuerier;
import derlin.symbiosart.utils.Common;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path( "/" )
public class QueryService{

    private MirflickrQuerier querier = new MirflickrQuerier( "mirflickr" );


    // curl -X POST -H 'Content-Type:application/json' --data '{"wedding": 2}' http://localhost:8000/rest/get/10
    @POST
    @Path( "/get/{nbr}" )
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML})
    public List<Common.Image> get( Common.TagsVector tagsVector, @PathParam( "nbr" ) int nbr ){
        return querier.getImages( tagsVector, nbr );
    }

    // curl -v -X POST -H 'Accept:application/xml' http://localhost:8000/rest/example/tagvector
    @POST
    @Path( "/example/tagsvector" )
    @Produces({MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML})
    public Common.TagsVector gelt( ){
        Common.TagsVector vector = new Common.TagsVector();
        vector.put( "newyork", 3 );
        vector.put( "wedding", 4 );
        vector.put( "sea", -1 );
        return vector;
    }
}//end class
