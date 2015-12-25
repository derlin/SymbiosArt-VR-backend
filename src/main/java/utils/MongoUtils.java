package utils;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */
public class MongoUtils{

    public final static String HOST = "dulcolax.local";
    public final static String DEFAULT_DB = "mydb";
    public final static String PHOTOS_COLLECTION = "test";

    private static final MongoClient MONGO_CLIENT = new MongoClient(HOST);
    private static final MongoDatabase MONGO_DB = MONGO_CLIENT.getDatabase( DEFAULT_DB );

    // ----------------------------------------------------

    public static MongoCollection<Document> getPhotoCollection( ){
        return getCollection( PHOTOS_COLLECTION );
    }
    public static MongoCollection<Document> getCollection( String coll ){
        return MONGO_DB.getCollection( coll );
    }

    // ----------------------------------------------------


    public static void main( String[] args ){
        FindIterable<Document> documents = getPhotoCollection().find();
        System.out.println(documents);
    }//end main

}//end class
