package derlin.symbiosart.api.user;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import derlin.symbiosart.pojo.User;
import derlin.symbiosart.utils.MongoUtils;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static derlin.symbiosart.pojo.User.MONGO_NAME_KEY;

/**
 * @author: Lucy Linder
 * @date: 04.01.2016
 */
public class UserApi{

    public static final String MONGO_COLL = "users";

    MongoCollection<Document> coll = MongoUtils.getCollection( MONGO_COLL );

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


    /* *****************************************************************
     *
     * ****************************************************************/
    public static void main( String[] args ){
        UserApi api = new UserApi();
        User test = api.getUser( "test" );
        test.getTagsVector().put( "moon", -5 );
        api.updateUser( "test", test );
    }//end main


}//end class
