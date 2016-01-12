package derlin.symbiosart.api.user;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import derlin.symbiosart.api.commons.Interfaces;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static derlin.symbiosart.api.commons.Constants.ID_KEY;
import static derlin.symbiosart.api.user.User.MONGO_NAME_KEY;

/**
 * @author: Lucy Linder
 * @date: 04.01.2016
 */
public class UsersApi implements Interfaces.IUsersApi{

    MongoCollection<Document> coll;


    public UsersApi( MongoCollection<Document> coll ){
        this.coll = coll;
    }


    public List<Document> getUsers(){

        FindIterable<Document> iterable = coll.find() //
                .projection( fields( include( ID_KEY, MONGO_NAME_KEY ) ) );

        return StreamSupport.stream( iterable.spliterator(), false )   //
                .collect( Collectors.toList() );

    }


    public User getUser( String id ){
        Document doc = coll.find( eq( ID_KEY, id ) ).first();
        if( doc == null ) return null;
        return User.fromMongoDoc( doc );
    }


    public void addUser( User user ){
        assert user.getId() == null;
        user.setId( new ObjectId().toString() );
        coll.insertOne( user.toMongoDoc() );
    }


    public boolean updateUser( String id, User user ){

        if( !user.getId().equals( id ) ){
            // id changed, error
            return false;

        }else{
            // update
            Document doc = user.toMongoDoc();
            coll.replaceOne( eq( ID_KEY, user.getId() ), doc );
            return true;
        }
    }


    public void removeUser( String id ){
        coll.deleteOne( eq( ID_KEY, id ) );
    }


}//end class
