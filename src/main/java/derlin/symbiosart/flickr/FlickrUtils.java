package derlin.symbiosart.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.tags.Tag;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This classes provides static wrappers to query
 * the flickr api.photo.search end point.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
public class FlickrUtils{

    private static final String APIKEY = "243075ed07d2c7f3c7ea7a408db48c62";
    private static final String SECRET = "96169140b26a8f27";
    private static final int SORT_ORDER = SearchParameters.INTERESTINGNESS_DESC;

    private static final Set<String> EXTRAS = new HashSet<>( Arrays.asList( "tags,machine_tags,url_o".split( "," ) ) );

    private static Flickr flickr = new Flickr( APIKEY, SECRET, new REST() );

    // ----------------------------------------------------


    public static List<Document> search( List<String> tags, int results ){
        return search( tags.toArray( new String[]{} ), results );
    }


    public static List<Document> search( String[] tags, int results ){

        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setExtras( EXTRAS );
        searchParameters.setTags( tags );
        searchParameters.setSort( SORT_ORDER );

        try{
            return flickr.getPhotosInterface()  //
                    .search( searchParameters, results, 1 ) //
                    .stream() //
                    .map( FlickrUtils::photoToMongoDoc )  //
                    .filter( d -> d != null ) //
                    .collect( Collectors.toList() );

        }catch( FlickrException e ){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ----------------------------------------------------

    // return null if tags empty or original photo unavailable
    private static Document photoToMongoDoc( Photo photo ){
        Document doc = new Document( "_id", photo.getId() );

        // first, check the image is still available
        try{
            doc.put( "url", photo.getOriginalUrl() );
        }catch( FlickrException e ){
            System.out.printf( "%s (%s) : unavailable%n", photo.getId(), photo.getUrl() );
            return null;
        }

        List<String> tags = photo.getTags().stream() //
                .map( Tag::getValue ).collect( Collectors.toList() );
        if( tags.isEmpty() ){
            System.out.printf( "%s (%s) : no tags%n", photo.getId(), photo.getUrl() );
            return null;
        }

        // get tags and owner
        doc.put( "tags", tags );
        doc.put( "owner", photo.getOwner().getId() );


        // optional fields
        if( photo.getTitle() != null ){
            doc.put( "title", photo.getTitle() );
        }

        if( photo.getDescription() != null ){
            doc.put( "description", photo.getDescription() );
        }

        if( photo.getOriginalFormat() != null ){
            doc.put( "originalFormat", photo.getOriginalFormat() );
        }

        return doc;
    }


    public static String getOriginalUrl( Photo p ){
        //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        return String.format( "https://farm%s.staticflickr.com/%s/%s_%s_o.%s", //
                p.getFarm(), p.getServer(), p.getId(), p.getSecret(), p.getOriginalFormat() );
    }


    /* *****************************************************************
     *
     * ****************************************************************/


    public static void main( String[] args ){
        String[] tags = new String[]{ "sea" };
        FlickrUtils api = new FlickrUtils();
        List<Document> list = api.search( tags, 100 );

        System.out.println( list.size() );
    }//end main

}//end class
