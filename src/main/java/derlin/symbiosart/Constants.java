package derlin.symbiosart;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import derlin.symbiosart.config.Config;
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
    public static final String MONGO_HOST = Config.getProperty( "mongoHost" );
    public final static String MONGO_DB = Config.getProperty( "mongoDb" );
    public final static Function<String, MongoCollection<Document>> MONGO_COLL_CREATOR = ( coll ) -> //
            new MongoClient( Constants.MONGO_HOST ).getDatabase( Constants.MONGO_DB ).getCollection( coll );

    public final static String MONGO_USERS_COLL = "users";
    public final static String MONGO_IMAGES_COLL = "images";


    // solr
    public static final String[] SOLR_INDEXED_FIELDS = new String[]{ ID_KEY, IMG_TAGS_KEY, URL_KEY, "originalFormat" };
    public static final String SOLR_DEFAULT_CORE = Config.getProperty( "solrCore" );
    public static final int MAX_TAGS_IN_SOLR_QUERY = 40;

    public static final Function<String, SolrClient> SOLR_CLIENT_CREATOR = ( core ) -> //
            new HttpSolrClient( Config.getProperty( "solrUrl" ) + "/" + core );

    public static final Function<SolrDocument, Document> SOLR_TO_DOC_CONVERTOR = ( r ) -> {
        Document doc = new Document();
        r.forEach( e -> doc.put( e.getKey(), e.getValue() ) );
        return doc;
    };


    // rest server
    public static final int SERVER_PORT = Config.getProperty( "serverPort", 80 );
    public static final String SERVER_HOST = Config.getProperty( "serverHost" );
    public static final String SERVER_URL = "http://" + SERVER_HOST + ":" + SERVER_PORT + "/";

}//end class
