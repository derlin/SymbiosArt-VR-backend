import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */
public class MongoConnector{
    private static MongoClient mongoClient = new MongoClient( "localhost" );
    private static MongoDatabase database = mongoClient.getDatabase( "mydb" );



    public static void main( String[] args ){
        MongoCollection<Document> collection = database.getCollection( "test" );

        String json = "{\"id\":\"2409783581\",\"owner\":\"11149526@N08\",\"secret\":\"1b332bc6fe\"," +
                "\"Server\":\"2335\",\"farm\":3,\"title\":\"Moustache\",\"description\":\"P'tain, mon bac à " +
                "fleurs!!\",\"tags\":[\"chat\",\"appart\"],\"machine_tags\":[],\"url\":\"https://farm3.staticflickr" +
                ".com/2335/2409783581_07413b3ca5_o.jpg\",\"height\":3008,\"width\":2008,\"geo\":{\"lat\":0," +
                "\"long\":0,\"accuracy\":0,\"context\":0},\"dates\":{\"upload\":\"2008-04-13 17:14:27+02:0\"," +
                "\"taken\":\"2008-04-13 16:09:12\"}}";

        //        collection.insertOne( docFromString( json ) );
//        long l =  insertAll( collection, "dataset.json" );
//        System.out.println( l );
        JsonContainer c = new JsonContainer( json );
        collection.insertOne( c.toMongoDoc() );
        System.out.println("ok");
    }//end main


    public static int insertAll( MongoCollection<Document> coll, String filepath ){

        // insert all from file, return the number of lines actually added
        try( Stream<String> stream = Files.lines( Paths.get( filepath ) ) ){
            return stream.mapToInt( s -> {
                Document d = docFromString( s );
                try{
                    coll.insertOne( d );
                    return 1;

                }catch( MongoException e ){
                    System.out.printf( "%s : error %s\n", d.getString( "_id" ), e.getMessage() );
                }
                    return 0;
            } ).sum();

        }catch( IOException e ){
            return -1;
        }
    }


    public static Document docFromString( String json ){
        Document doc = Document.parse( json );
        String id = doc.getString( "id" );
        doc.append( "_id", id );
        doc.remove( "id" );
        return doc;

    }
}//end class
