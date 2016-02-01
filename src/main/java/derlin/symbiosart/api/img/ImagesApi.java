package derlin.symbiosart.api.img;

import com.mongodb.client.MongoCollection;
import derlin.symbiosart.Constants;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.commons.TagsVector;
import derlin.symbiosart.api.commons.exceptions.NotFoundException;
import derlin.symbiosart.api.commons.exceptions.UnexpectedError;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.bson.Document;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Lucy Linder
 * @date: 06.01.2016
 */
public class ImagesApi implements Interfaces.IIMagesApi{

    private static Logger log = org.slf4j.LoggerFactory.getLogger( ImagesApi.class );

    private SolrClient solrClient;
    private MongoCollection<Document> mongoClient;
    private String fl = String.join( ",", Constants.SOLR_INDEXED_FIELDS );
    private Random rand = new Random();


    public ImagesApi( SolrClient solrClient, MongoCollection<Document> mongoClient ){
        this.solrClient = solrClient;
        this.mongoClient = mongoClient;
    }


    @Override
    public Document getDetails( String id ) throws NotFoundException{
        Document doc = mongoClient.find( new Document( Constants.ID_KEY, id ) ).first();
        if( doc == null ){
            log.error( String.format( "Request for document %s failed: no such document", id ) );
            throw new NotFoundException( id );
        }
        return doc;
    }


    @Override
    public List<Document> getSuggestions( TagsVector tagsVector, int nbr ) throws UnexpectedError{

        SolrQuery query = new SolrQuery();

        try{

            query.set( "fl", fl );
            query.set( "start", "0" );
            query.set( "rows", nbr );
            query.set( "defType", "edismax" );

            log.debug( "---------------" );

            String q;
            if( tagsVector.isEmpty() ){
                q = "*:*";
                query.set( "sort", "random_" + rand.nextInt( 1000 ) + " desc" );

            }else{
                q = processTagsVector( tagsVector ) //
                        .entrySet().stream() //
                        .map( k -> String.format( "(%stags:%s)^%d ",  //
                                k.getValue() < 0 ? "-" : "", //
                                k.getKey(), Math.abs( k.getValue() ) ) )//
                        .collect( Collectors.joining( " " ) );
            }

            query.set( "q", q );

            log.debug( "solr query: " + query.toString() );

            QueryResponse response = solrClient.query( query );

            List<Document> list = response.getResults().stream() //
                    .map( Constants.SOLR_TO_DOC_CONVERTOR::apply ).collect( Collectors.toList() );

            log.info( "returned " + list.size() + " documents." );
            log.debug( "---------------" );
            return list;

        }catch( SolrServerException | IOException e ){
            e.printStackTrace();
            log.error( e.toString() );
            throw new UnexpectedError( e );
        }
    }


    /* *****************************************************************
     * private utils
     * ****************************************************************/


    private TagsVector processTagsVector( TagsVector original ){
        TagsVector tv = new TagsVector();

        // the map is implemented as a treemap, so the keys will always be
        // sorted alphabetically. To avoid getting always the same result,
        // shuffe the entries before sort
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>( original.entrySet() );
        Collections.shuffle( entries );

        // now that we have a random list, sort and select the first X entries.
        entries.stream()  //
                .sorted( ( e1, e2 ) -> {
                    int x = Math.abs( e2.getValue() );
                    int y = Math.abs( e1.getValue() );
                    return ( x < y ) ? -1 : ( ( x == y ) ? 0 : 1 );
                } ) //
                .limit( Constants.MAX_TAGS_IN_SOLR_QUERY ) //
                .forEach( e -> tv.put( e.getKey(), e.getValue()) );
//                .forEach( e -> tv.put( e.getKey(), e.getValue().compareTo( 0 ) ) );

        log.debug( "original tv: " + original.toPrettyString() );
        log.debug( "filtered tv: " + tv.toPrettyString() );
        return tv;
    }

}//end class
