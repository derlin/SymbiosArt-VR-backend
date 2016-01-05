package derlin.symbiosart.api.img;

import com.google.gson.Gson;
import derlin.symbiosart.jetty.WebAppLauncher;
import derlin.symbiosart.pojo.TagsVector;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Lucy Linder
 * @date: 25.12.2015
 */
public class MirflickrApi extends Api{

    private static final String SOLR_CORE = "mirflickr";

    @Override
    public String getSolrCore(){
        return SOLR_CORE;
    }


    @Override
    public String getMongoColl(){
        return "";
    }


    @Override
    public List<Document> getSuggestions( TagsVector tagsVector, int nbr ){

        // avoid asking mongo: wrap solr results into a bson
        return solrQuery( tagsVector, nbr ).entrySet().stream() //
                .map( m -> {
                    Document d = new Document( "_id", m.getKey() );
                    d.put( "tags", m.getValue() );
                    // add custom url
                    d.put("url", String.format( "%smirflickr/im%s.jpg",WebAppLauncher.SERVER_URL, m.getKey()) );
                    return d;
                } ) //
                .collect( Collectors.toList() );

    }


    public static void main( String[] args ){
        TagsVector vector = new TagsVector();
        vector.put( "newyork", 3 );
        vector.put( "wedding", 4 );
        vector.put( "sea", -1 );

        System.out.println(new Gson().toJson( vector ));

//        MirflickrApi querier = new MirflickrApi();
//        List<Document> images = querier.getSuggestions( vector, 3 );
//        images.forEach( System.out::println );
    }//end main


}//end class