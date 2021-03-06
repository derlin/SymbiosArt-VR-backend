package derlin.symbiosart.api.batch;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import derlin.symbiosart.api.commons.WordNetFilter;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static derlin.symbiosart.Constants.*;

/**
 * This class provides a simple command-line utility to
 * index/insert documents into the mongo and solr servers
 * provided by {@link derlin.symbiosart.api.ApiProvider}.
 * <p>
 * Usage: <br />
 * <pre>
 *     java BatchInserter <path to json file> <mongo collection> <solr core>
 * </pre>
 * <p>
 * Arguments:
 * <ol>
 * <li>path: mandatory, the path to a valid json file, one document per line</li>
 * <li>mongo: the name of the mongo collection to use. Will be created
 * if needed. Provide an empty string ("") if you want only solr indexing</li>
 * <li>solr: the name of the core. Provide an empty string ("") if you want only mongo insertion.</li>
 * </ol>
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
public class BatchInserter{

    private MongoCollection<Document> mongo;
    private SolrClient solr;
    private WordNetFilter wnFilter;

    /* *****************************************************************
     * constructor
     * ****************************************************************/


    public BatchInserter( String mongoCollection, String solrCore ){
        if( mongoCollection != null && !mongoCollection.isEmpty() ) mongo = MONGO_COLL_CREATOR.apply( mongoCollection );
        if( solrCore != null && !solrCore.isEmpty() ) solr = SOLR_CLIENT_CREATOR.apply( solrCore );

        try{
            wnFilter = new WordNetFilter();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }


    /* *****************************************************************
     * public functions
     * ****************************************************************/


    public long insertAll( String filepath ){

        // insert all from file, return the number of lines actually added
        try( Stream<String> stream = Files.lines( Paths.get( filepath ) ) ){
            return stream  //
                    .map( this::fromJson )  //
                    .map( this::insertOne ).filter( r -> r ) //
                    .count();

        }catch( IOException e ){
            e.printStackTrace();
        }

        return -1;
    }

    // ----------------------------------------------------


    public long indexAll( String filepath ){

        // index all from file, return the number of lines actually added
        try( Stream<String> stream = Files.lines( Paths.get( filepath ) ) ){
            long sum = stream //
                    .map( this::fromJson )  //
                    .map( this::indexOne ) //
                    .filter( r -> r ) //
                    .count();

            if( sum > 0 ){
                solr.commit();
            }

            return sum;

        }catch( IOException | SolrServerException e ){
            e.printStackTrace();
        }

        return -1;
    }

    // ----------------------------------------------------


    public long insertIndexAll( String filepath ){

        // insert and index all from file, return the number of lines actually added
        try( Stream<String> stream = Files.lines( Paths.get( filepath ) ) ){
            long sum = stream //
                    .map( this::fromJson )  //
                    .map( d -> insertOne( d ) ? d : null )
                    // don't index docs if insert failed
                    .filter( d -> d != null ) //
                    .map( this::indexOne )  //
                    .filter( r -> r ) //
                    .count();

            if( sum > 0 ){
                solr.commit();
            }

            return sum;

        }catch( IOException | SolrServerException e ){
            e.printStackTrace();
        }

        return -1;
    }

    /* *****************************************************************
     * private utils
     * ****************************************************************/


    private boolean insertOne( Document doc ){

        try{
            mongo.insertOne( doc );
            System.out.printf( "%s inserted.\n", doc.get( ID_KEY ).toString() );
            return true;

        }catch( MongoException e ){
            System.out.printf( "%s : error inserting into mongo %s\n", doc.getString( ID_KEY ), e.getMessage() );
        }
        return false;
    }

    // ----------------------------------------------------


    private boolean indexOne( Document mongoDoc ){
        try{
            // construct the solr doc
            SolrInputDocument solrDoc = new SolrInputDocument();
            for( String key : SOLR_INDEXED_FIELDS ){
                if( mongoDoc.containsKey( key ) ){
                    solrDoc.addField( key, mongoDoc.get( key ) );
                }
            }//end for

            solrProcessDoc( mongoDoc, solrDoc );

            // if empty, don't index
            if( solrDoc.isEmpty() ){
                System.out.println( "Empty solr doc : " + mongoDoc );
                return false;
            }

            solr.add( solrDoc );
            return true;

        }catch( IOException | SolrServerException e ){
            e.printStackTrace();
            return false;
        }
    }




    private void solrProcessDoc( Document mongoDoc, SolrInputDocument doc ){
        List<String> tags = ( List<String> ) mongoDoc.get( IMG_TAGS_KEY );
        doc.removeField( "tags" );
        doc.addField( "row_tags", tags );
        doc.addField( "tags", wnFilter.filterTags( tags ) );
    }


    // ----------------------------------------------------


    private Document fromJson( String json ){
        Document doc = Document.parse( json );
        if( doc.containsKey( "id" ) ){
            doc.put( ID_KEY, doc.get( "id" ) );
            doc.remove( "id" );
        }
        return doc;
    }


    /* *****************************************************************
     * main method
     * ****************************************************************/


    public static void main( String[] args ){


        if( args.length != 3 ){
            System.out.println( "usage: <path to json file> <mongo collection> <solr core>" );
            System.out.println( "use empty string for only indexing or inserting." );
            System.exit( 0 );
        }

        String path = args[ 0 ], mongoColl = args[ 1 ], solrCore = args[ 2 ];

        BatchInserter inserter = new BatchInserter( mongoColl, solrCore );
        long sum;

        if( mongoColl.isEmpty() ){
            sum = inserter.indexAll( path );
            System.out.println( "indexing..." );
        }else if( solrCore.isEmpty() ){
            sum = inserter.insertAll( path );
            System.out.println( "inserting..." );

        }else{
            System.out.println( "inserting and indexing..." );
            sum = inserter.insertIndexAll( path );
        }

        System.out.printf( "done. Successfully treated %d records.\n", sum );

    }//end main


}//end class
