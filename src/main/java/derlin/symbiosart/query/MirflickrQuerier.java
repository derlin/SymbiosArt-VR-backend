package derlin.symbiosart.query;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import derlin.symbiosart.utils.Common;
import derlin.symbiosart.utils.SolrUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Lucy Linder
 * @date: 25.12.2015
 */
public class MirflickrQuerier{

    private SolrClient solrClient;


    public MirflickrQuerier( String core ){
        this.solrClient = SolrUtils.createClient( core );
    }


    public List<Common.Image> getImages( Common.TagsVector tagsVector ){
        return getImages( tagsVector, 10 );
    }


    public List<Common.Image> getImages( Common.TagsVector tagsVector, int nbr ){
        SolrQuery query = new SolrQuery();

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

            return response.getResults().stream()  //
                    .map( r -> new Common.Image( ( String ) r.get( "id" ), ( ( List<String> ) r.get( "tags" ) ) ) )//
                    .collect( Collectors.toList() );

        }catch( SolrServerException | IOException e ){
            e.printStackTrace();
        }

        return null;
    }


    public static void main( String[] args ){
        Common.TagsVector vector = new Common.TagsVector();
        vector.put( "newyork", 3 );
        vector.put( "wedding", 4 );
        vector.put( "sea", -1 );


        MirflickrQuerier querier = new MirflickrQuerier( "mirflickr" );
        List<Common.Image> images = querier.getImages( vector, 3 );
        images.forEach( System.out::println );
    }//end main


}//end class
