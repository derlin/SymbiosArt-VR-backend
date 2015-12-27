package derlin.symbiosart.api;

import derlin.symbiosart.pojo.TagsVector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Lucy Linder
 * @date: 20.12.2015
 */
public class YahooFlickrApi extends Api{


    private static final String MONGO_PHOTO_COLLECTION = "test";
    private static final String SOLR_CORE = "core1";

    // ----------------------------------------------------


    public YahooFlickrApi(){
        super();
    }


    @Override
    public String getSolrCore(){
        return SOLR_CORE;
    }


    @Override
    public String getMongoColl(){
        return MONGO_PHOTO_COLLECTION;
    }

    // ----------------------------------------------------


    public static void main( String[] args ){
        TagsVector vector = new TagsVector();
        vector.put( "newyork", 3 );
        vector.put( "wedding", 4 );
        vector.put( "sea", 1 );
        vector.put( "lions", 1 );

        YahooFlickrApi api = new YahooFlickrApi();
        List<String> urls = api.getSuggestions( vector ).stream() //
                .map( d -> d.getString( "url" ) )  //
                .collect( Collectors.toList() );

        urls.stream().forEach( u -> System.out.printf( "<img src='%s' width=300 />\n", u ) );
    }//end main


}//end class

