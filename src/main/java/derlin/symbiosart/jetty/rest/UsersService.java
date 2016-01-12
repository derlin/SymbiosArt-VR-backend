package derlin.symbiosart.jetty.rest;

import derlin.symbiosart.api.ApiProvider;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.user.User;
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
@Path( "/user" )
public class UsersService{

    private Interfaces.IUsersApi api = new ApiProvider().getUsersApi();


    // curl http://localhost:8680/rest/user/all
    @GET
    @Path( "/all" )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
//    public Map<String,String> getNameList(){
    public List<Document> getNameList(){
        return api.getUsers();
    }


    // curl http://localhost:8680/rest/user
    @GET
    @Path( "/{id}" )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public User getUser( @PathParam( "id" ) String id ){
        System.out.println("getting id " + id);
        return api.getUser( id );
    }


    // either add or update
    @POST
    @Path( "/" )
    @Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public String addUpdateUser( User user ){
        if( user.getId() == null ){
            System.out.println("adding user " + user);
            api.addUser( user );
        }else{
            System.out.println("updating user " + user);
            api.updateUser( user.getId(), user );
        }
        return user.getId();
    }


    // curl -X POST -H 'Content-Type:application/json' --data '{"name":"test","tags_vector":{"newyork":3,"sea":-1} }'
    // http://localhost:8680/rest/user
    @POST
    @Path( "/add" )
    @Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public String addUser( User user ){
        assert user.getId() == null;
        api.addUser( user );
        return user.getId();
    }


    // curl -X POST -H 'Content-Type:application/json' --data '{"name":"test", "tags_vector":{"wedding": 2}}'
    // http://localhost:8680/rest/user/
    @POST
    @Path( "/update" )
    @Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public boolean updateUser( User user ){
        return user.getId() != null && api.updateUser( user.getId(), user );
    }


    // curl -X DELETE  http://localhost:8680/rest/user/test
    @DELETE
    @Path( "/{id}" )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public void deleteUser( @PathParam( "id" ) String id ){
        api.removeUser( id );
    }


}//end class
