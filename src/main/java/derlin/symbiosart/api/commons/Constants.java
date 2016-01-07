package derlin.symbiosart.api.commons;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.bson.Document;

import java.util.function.Function;

/**
 * @author: Lucy Linder
 * @date: 06.01.2016
 */
public class Constants{

    // common keys
    public static final String ID_KEY = "_id";
    public static final String IMG_TAGS_KEY = "tags";
    public static final String URL_KEY = "url";

    // mongo
    public static final String MONGO_HOST = "localhost";
    public final static String MONGO_DB = "symbiosart"; // mydb
    public final static Function<String, MongoCollection<Document>> MONGO_COLL_CREATOR = ( coll ) -> //
            new MongoClient( Constants.MONGO_HOST ).getDatabase( Constants.MONGO_DB ).getCollection( coll );

    public final static String MONGO_USERS_COLL = "users";
    public final static String MONGO_IMAGES_COLL = "images";


    // solr
    public final static String SOLR_HOST = "localhost"; //"dulcolax.local";
    public final static int SOLR_PORT = 8983;
    public static final String[] SOLR_INDEXED_FIELDS = new String[]{ ID_KEY, IMG_TAGS_KEY, URL_KEY };
    public static final String SOLR_DEFAULT_CORE = "symbiosart";

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
