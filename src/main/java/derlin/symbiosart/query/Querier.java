package derlin.symbiosart.query;

import com.mongodb.client.MongoCollection;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.bson.Document;
import derlin.symbiosart.utils.MongoUtils;
import derlin.symbiosart.utils.SolrUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * @author: Lucy Linder
 * @date: 20.12.2015
 */
public class Querier{
    private MongoCollection<Document> photoCollection;
    private SolrClient solrClient;

    // ----------------------------------------------------


    public Querier(){
        photoCollection = MongoUtils.getPhotoCollection();
        solrClient = SolrUtils.createClient( "core1" );
    }


    public List<String> getMatchingUrls( Map<String, Integer> tagsVector ){
        SolrQuery query = new SolrQuery();

        try{

            query.set( "fl", "id" );
            query.set( "start", "0" );
            query.set( "rows", "10" );

            String q = tagsVector.entrySet() //
                    .stream() //
                    .map( k -> String.format( "tags:%s^%d ", k.getKey(), k.getValue() ) )  //
                    .collect( Collectors.joining( " " ) );


            query.set( "q", q );

            System.out.println( query.toString() );
            //            if(true) return null;

            QueryResponse response = solrClient.query( query );

            return response.getResults().stream()  //
                    .map( r -> ( String ) r.get( "id" ) )  //
                    .collect( Collectors.toList() );

        }catch( SolrServerException | IOException e ){
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------------------------------------


    public String getIdFromUrl( String id ){
        Document result = photoCollection.find( eq( "_id", id ) ) //
                .projection( fields( include( "url" ) ) ).first();
//        System.out.println( result );
        return ( String ) result.get( "url" );

    }


    public static void main( String[] args ){
        Map<String, Integer> vector = new TreeMap<>();
        vector.put( "newyork", 3 );
        vector.put( "wedding", 4 );
        vector.put( "sea", 1 );
        vector.put( "lions", 1 );

        Querier querier = new Querier();
        List<String> urls = querier.getMatchingUrls( vector );

        urls.stream().map( querier::getIdFromUrl ).forEach( u -> System.out.printf( "<img src='%s' width=300 />\n", u
        ) );
    }//end main


}//end class

