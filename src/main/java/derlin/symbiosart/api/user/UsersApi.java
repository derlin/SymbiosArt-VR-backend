package derlin.symbiosart.api.user;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import derlin.symbiosart.api.commons.Interfaces;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static derlin.symbiosart.api.commons.Constants.ID_KEY;
import static derlin.symbiosart.api.user.User.MONGO_NAME_KEY;

/**
 * This class implements the CRUD operations of the
 * IUserApi. It manipulates only {@link User} objects.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
public class UsersApi implements Interfaces.IUsersApi{

    private static Logger log = org.slf4j.LoggerFactory.getLogger( UsersApi.class );

    MongoCollection<Document> mongoCollection;


    public UsersApi( MongoCollection<Document> mongoCollection ){
        this.mongoCollection = mongoCollection;
    }


    public List<Document> getUsers(){

        FindIterable<Document> iterable = mongoCollection.find() //
                .projection( fields( include( ID_KEY, MONGO_NAME_KEY ) ) );

        return StreamSupport.stream( iterable.spliterator(), false )   //
                .collect( Collectors.toList() );

    }


    public User getUser( String id ){
        Document doc = mongoCollection.find( eq( ID_KEY, id ) ).first();
        if( doc == null ){
            log.warn( "error finding user with id '%s'", id );
            return null;
        }
        return User.fromMongoDoc( doc );
    }


    public void addUser( User user ){
        assert user.getId() == null;
        user.setId( new ObjectId().toString() );
        mongoCollection.insertOne( user.toMongoDoc() );
        log.info( "%s added", user.getId() );
    }


    public boolean updateUser( String id, User user ){

        if( !user.getId().equals( id ) ){
            // id changed, error
            log.warn( "error: id mismatch '%s' '%s'", id, user );
            return false;

        }else{
            // update
            Document doc = user.toMongoDoc();
            mongoCollection.replaceOne( eq( ID_KEY, user.getId() ), doc );
            log.info( "%s updated", id );
            return true;
        }
    }


    public void removeUser( String id ){
        mongoCollection.deleteOne( eq( ID_KEY, id ) );
    }


}//end class
