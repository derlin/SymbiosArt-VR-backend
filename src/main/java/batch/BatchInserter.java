package batch;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.bson.Document;
import utils.DataContainer;
import utils.MongoUtils;
import utils.SolrUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author: Lucy Linder
 * @date: 20.12.2015
 */
public class BatchInserter{

    private MongoCollection<Document> photoCollection;
    private SolrClient solrClient;

    // ----------------------------------------------------


    public BatchInserter(){
        photoCollection = MongoUtils.getPhotoCollection();
        solrClient = SolrUtils.createClient("core1");
    }

    // ----------------------------------------------------


    public int insertAll( String filepath ){

        // insert all from file, return the number of lines actually added
        try( Stream<String> stream = Files.lines( Paths.get( filepath ) ) ){
            int sum = stream.mapToInt( s -> insertOne( s ) ? 1 : 0 ).sum();
            if( sum > 0 ){
                solrClient.commit();
            }

            return sum;

        }catch( IOException e ){
            e.printStackTrace();

        }catch( SolrServerException e ){
            e.printStackTrace();
        }

        return -1;
    }

    // ----------------------------------------------------


    public int indexAll( String filepath ){

        // insert all from file, return the number of lines actually added
        try( Stream<String> stream = Files.lines( Paths.get( filepath ) ) ){
            int sum = stream.map( DataContainer::new )  //
                    .map( DataContainer::toSolrInputDoc ) //
                    .mapToInt( d -> {
                try{
                    solrClient.add( d );
                    System.out.println( d.get( "id" ) + " added.");
                    return 1;
                }catch( SolrServerException | IOException e ){
                    e.printStackTrace();
                    return 0;
                }
            } ).sum();

            if( sum > 0 ){
                solrClient.commit();
            }

            return sum;

        }catch( IOException | SolrServerException e ){
            e.printStackTrace();
        }

        return -1;
    }

    // ----------------------------------------------------


    private boolean insertOne( String json ){
        DataContainer data = new DataContainer( json );
        Document mongoDoc = data.toMongoDoc();

        try{
            photoCollection.insertOne( mongoDoc );
            solrClient.add( data.toSolrInputDoc() );
            System.out.printf( "%s inserted.\n", mongoDoc.get( "_id" ) );
            return true;

        }catch( MongoException e ){
            System.out.printf( "%s : error inserting into mongo %s\n", mongoDoc.getString( "_id" ), e.getMessage() );

        }catch( SolrServerException e ){
            System.out.printf( "%s : error indexing into solr %s\n", mongoDoc.getString( "_id" ), e.getMessage() );
            photoCollection.deleteOne( mongoDoc );
            e.printStackTrace();

        }catch( IOException e ){
            System.out.printf( "error ?? %s\n", e.getMessage() );

        }
        return false;
    }

    // ----------------------------------------------------


    public static void main( String[] args ){
        if( args.length != 1 ){
            System.out.println( "Missing argument: path to json file" );
            System.exit( 0 );
        }

        BatchInserter inserter = new BatchInserter();

        int sum = inserter.indexAll( args[ 0 ] );

        System.out.printf( "done. Inserted %d records.\n", sum );

    }//end main


}//end class
