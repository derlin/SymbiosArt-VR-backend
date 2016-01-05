package derlin.symbiosart;

import com.mongodb.client.FindIterable;
import derlin.symbiosart.utils.MongoUtils;
import org.bson.Document;

import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * @author: Lucy Linder
 * @date: 04.01.2016
 */
public class Test{

    public static void main( String[] args ){
        FindIterable<Document> iterable = MongoUtils.getCollection( "users" ).find().projection( fields( include(
                "_id" ) ) );
        StreamSupport.stream( iterable.spliterator(), false )   //
                .map( d -> d.getString( "_id" ) )   //
                .forEach( System.out::println );

    }//end main



}//end class
