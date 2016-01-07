package derlin.symbiosart.api.img;

import com.mongodb.client.MongoCollection;
import derlin.symbiosart.api.commons.Constants;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.commons.TagsVector;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Lucy Linder
 * @date: 06.01.2016
 */
public class ImagesApi implements Interfaces.IIMagesApi{

    private SolrClient solrClient;
    private MongoCollection<Document> mongoClient;
    private String fl = String.join(",", Constants.SOLR_INDEXED_FIELDS);;

    public ImagesApi( SolrClient solrClient, MongoCollection<Document> mongoClient ){
        this.solrClient = solrClient;
        this.mongoClient = mongoClient;
    }


    @Override
    public List<Document> getSuggestions( TagsVector tagsVector, int nbr ){

        SolrQuery query = new SolrQuery();

        try{

            query.set( "fl", fl );
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

            return response.getResults().stream() //
                    .map( Constants.SOLR_TO_DOC_CONVERTOR::apply )
                    // add this server url
                    //                    .map( doc -> {
                    //                        doc.put( "url", String.format( "%simages/%s.jpg", //
                    //                                Constants.SERVER_URL, //
                    //                                doc.getString( Constants.ID_KEY ) ) );
                    //                        return doc;
                    //                    } ) //
                    .collect( Collectors.toList() );


        }catch( SolrServerException | IOException e ){
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /* *****************************************************************
     * private utils
     * ****************************************************************/


}//end class
