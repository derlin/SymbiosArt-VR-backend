package derlin.symbiosart.api.user;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import derlin.symbiosart.api.commons.Interfaces;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
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


    public List<String> getUsers(){

        FindIterable<Document> iterable = coll.find() //
                .projection( fields( include( MONGO_NAME_KEY ) ) );

        List<String> names = StreamSupport.stream( iterable.spliterator(), false )   //
                .map( d -> d.getString( MONGO_NAME_KEY ) )   //
                .collect( Collectors.toList() );

        return names;
    }


    public User getUser( String id ){
        Document doc = coll.find( eq( MONGO_NAME_KEY, id ) ).first();
        if(doc == null) return null;
        return User.fromMongoDoc( doc );
    }


    public void addUser( User user ){
        coll.insertOne( user.toMongoDoc() );
    }

    public boolean updateUser( String id, User user ){

        if( !user.getName().equals( id ) ){
            // id changed: remove then insert
            coll.deleteOne( eq( MONGO_NAME_KEY, id ) );
            coll.insertOne( user.toMongoDoc() );
            return true;

        }else{
            // update
            Document doc = user.toMongoDoc();
            coll.replaceOne( eq( MONGO_NAME_KEY, user.getName() ), doc );
            return true;
        }
    }


    public void removeUser( String id ){
        coll.deleteOne( eq( MONGO_NAME_KEY, id ) );
    }

}//end class
