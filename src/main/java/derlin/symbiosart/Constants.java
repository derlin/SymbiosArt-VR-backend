package derlin.symbiosart;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.bson.Document;

import java.util.function.Function;

/**
 * This class gathers all the configuration constants
 * used throughout the application (server hosts,
 * indexed fields, etc) allowing a simple/quick
 * customization.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 06.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
public class Constants{

    // common strings
    public static final String ID_KEY = "_id";
    public static final String IMG_TAGS_KEY = "tags";
    public static final String URL_KEY = "url";

    // mongo
    public static final String MONGO_HOST = "localhost";
    public final static String MONGO_DB = "symbiosart";
    public final static Function<String, MongoCollection<Document>> MONGO_COLL_CREATOR = ( coll ) -> //
            new MongoClient( Constants.MONGO_HOST ).getDatabase( Constants.MONGO_DB ).getCollection( coll );

    public final static String MONGO_USERS_COLL = "users";
    public final static String MONGO_IMAGES_COLL = "images";


    // solr
    public final static String SOLR_HOST = "localhost"; //"dulcolax.local";
    public final static int SOLR_PORT = 8983;
    public static final String[] SOLR_INDEXED_FIELDS = new String[]{ ID_KEY, IMG_TAGS_KEY, URL_KEY, "originalFormat" };
    public static final String SOLR_DEFAULT_CORE = "symbiosart";
    public static final int MAX_TAGS_IN_SOLR_QUERY = 40;

    public static final Function<String, SolrClient> SOLR_CLIENT_CREATOR = ( core ) -> //
            new HttpSolrClient( String.format( "http://%s:%d/solr/%s", SOLR_HOST, SOLR_PORT, core ) );

    public static final Function<SolrDocument, Document> SOLR_TO_DOC_CONVERTOR = ( r ) -> {
        Document doc = new Document();
        r.forEach( e -> doc.put( e.getKey(), e.getValue() ) );
        return doc;
    };


    // rest server
    public static final int SERVER_PORT = 8680;
    public static final String SERVER_URL = "http://192.168.0.23:" + SERVER_PORT + "/";

}//end class
