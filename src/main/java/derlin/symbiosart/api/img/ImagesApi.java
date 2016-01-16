package derlin.symbiosart.api.img;

import com.mongodb.client.MongoCollection;
import derlin.symbiosart.api.commons.Constants;
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
import java.util.List;
import java.util.Random;
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


    public static TagsVector processTagsVector( TagsVector original ){
        TagsVector tv = new TagsVector();

        original.entrySet().stream()  //
                .sorted( ( e1, e2 ) -> {
                    int x = Math.abs( e2.getValue() );
                    int y = Math.abs( e1.getValue() );
                    return ( x < y ) ? -1 : ( ( x == y ) ? 0 : 1 );
                } ) //
                .limit( Constants.MAX_TAGS_IN_SOLR_QUERY ) //
                .forEach( e -> tv.put( e.getKey(), e.getValue().compareTo( 0 ) ) );

        log.debug( "original tv: " + original.toPrettyString() );
        log.debug( "filtered tv: " + tv.toPrettyString() );
        return tv;
    }
    //"(tags:aguila)^1  (tags:aigle)^-4  (tags:angle)^1  (tags:anglesanglesangles)^1  (tags:animal)^0
    // (tags:animali)^1  (-tags:animals)^1  (tags:architecture)^1  (tags:architectuur)^1  (tags:art)^1
    // (tags:atmosphere)^1  (tags:aves)^1  (tags:avond)^1  (tags:belgique)^1  (tags:belgium)^1  (tags:belgië)^1
    // (tags:bird)^1  (tags:black)^1  (tags:blackandwhite)^1  (tags:blackandwhiteonly)^1  (tags:blackdiamond)^1
    // (tags:blackwhite)^1  (tags:building)^1  (tags:bw)^2  (tags:bwgallery)^1  (-tags:caballo)^1
    // (tags:cabeciblanco)^1  (tags:calatrava)^1  (-tags:camargue)^1  (-tags:camarguepferde)^1  (-tags:camarque)^1
    // (tags:canon)^1  (tags:canoneos5dmarkii)^1  (tags:canonnl)^1  (-tags:cheval)^1  (-tags:chevaux)^1
    // (tags:compositie)^1  (tags:composition)^1  (tags:contrast)^1  (-tags:creature)^1  (tags:d3200)^1
    // (tags:details)^1  (tags:diagonaal)^1  (tags:dynamic)^1  (tags:dynamisch)^1  (tags:eagle)^1  (-tags:effecte)^1
    // (tags:europa)^1  (tags:europe)^1  (-tags:explore)^1  (tags:fotografie)^1  (-tags:franca)^1  (-tags:france)^1
    // (-tags:francia)^1  (-tags:frankreich)^1  (tags:gare)^1  (tags:gebouw)^1  (tags:geometrie)^1
    // (tags:geometriegeometry)^1  (tags:geometry)^1  (tags:grafisch)^1  (tags:graphic)^1  (tags:graphicphoto)^1
    // (tags:guillemins)^1  (-tags:horse)^1  (tags:jeroenvandewiel)^1  (tags:jeronim)^1  (tags:jeronim01)^1
    // (-tags:landscape)^1  (-tags:landschaft)^1  (tags:licht)^1  (tags:liege)^1  (tags:light)^1  (tags:lijnen)^1
    // (tags:lines)^1  (tags:linescurves)^1  (tags:liège)^1  (tags:liègeguillemins)^1  (tags:luik)^1  (tags:nacht)^1
    // (-tags:natur)^1  (-tags:naturalezza)^1  (-tags:nature)^1  (tags:night)^1  (tags:nikkor)^1  (tags:nikon)^1
    // (-tags:paisaje)^1  (tags:perspectief)^1  (tags:perspective)^1  (-tags:pferd)^1  (tags:photography)^1
    // (tags:pov)^1  (tags:prey)^1  (-tags:provenca)^1  (-tags:provence)^1  (tags:pygargue)^1  (tags:railway)^1
    // (tags:rapace)^1  (tags:rapaz)^1  (-tags:reisen)^1  (-tags:roba66)^1  (tags:santiagocalatrava)^1
    // (tags:station)^1  (tags:tailed)^1  (-tags:textur)^1  (-tags:texture)^1  (-tags:tier)^1  (-tags:tiere)^1
    // (-tags:tourism)^1  (-tags:trabalho)^1  (tags:train)^1  (-tags:travel)^1  (tags:trein)^1  (-tags:urlaub)^1
    // (-tags:visit)^1  (-tags:voyages)^1  (tags:wallonië)^1  (tags:white)^1  (tags:zwartwit)^1 ",

}//end class
