package derlin.symbiosart.api;

import com.mongodb.client.MongoCollection;
import derlin.symbiosart.pojo.TagsVector;
import derlin.symbiosart.utils.MongoUtils;
import derlin.symbiosart.utils.SolrUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

/**
 * @author: Lucy Linder
 * @date: 27.12.2015
 */
public abstract class Api{
    public static final int DEFAULT_NBR_ROWS = 10;

    protected SolrClient solrClient;
    protected MongoCollection<Document> mongoClient;


    public Api(){
        this.solrClient = SolrUtils.createClient( getSolrCore() );
        this.mongoClient = MongoUtils.getCollection( getMongoColl() );
    }


    public List<Document> getSuggestions( TagsVector tagsVector ){
        return getSuggestions( tagsVector, DEFAULT_NBR_ROWS );
    }


    public List<Document> getSuggestions( TagsVector tagsVector, int nbr ){
        Map<String, List<String>> map = solrQuery( tagsVector, nbr );

        return map.entrySet().stream() //
                .map( m -> getMetaFromId( m.getKey() ) ) //
                .filter( Objects::nonNull )  // remove ids without matches in mongo
                .collect( Collectors.toList() );

    }


    // ----------------------------------------------------

    protected Map<String, List<String>> solrQuery( TagsVector tagsVector ){
        return solrQuery( tagsVector, DEFAULT_NBR_ROWS );
    }

    protected Map<String, List<String>> solrQuery( TagsVector tagsVector, int nbr ){
        SolrQuery query = new SolrQuery();

        Map<String, List<String>> solrResults = new TreeMap<>();

        try{

            query.set( "fl", "id,tags" );
            query.set( "start", "0" );
            query.set( "rows", nbr );

            String q = tagsVector.entrySet().stream() //
                    .map( k -> String.format( "(%stags:%s)^%d ",  //
                            k.getValue() < 0 ? "-" : "", //
                            k.getKey(), Math.abs( k.getValue() ) ) )//
                    .collect( Collectors.joining( " " ) );


            query.set( "q", q );

            System.out.println( query.toString() );

            QueryResponse response = solrClient.query( query );

            response.getResults().forEach(  //
                    r -> solrResults.put(      //
                            r.get( "id" ).toString(),  //
                            ( ( List<String> ) r.get( "tags" ) ) ) );

            return solrResults;

        }catch( SolrServerException | IOException e ){
            e.printStackTrace();
        }

        return null;
    }

    // ----------------------------------------------------


    protected Document getMetaFromId( Object id ){
        return mongoClient.find( eq( "_id", id ) ).first();
    }


    public abstract String getSolrCore();

    public abstract String getMongoColl();

}//end interface
