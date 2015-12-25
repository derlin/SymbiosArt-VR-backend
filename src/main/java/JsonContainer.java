import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.solr.common.SolrInputDocument;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */


public class JsonContainer{

    public static final Gson GSON = new GsonBuilder().setDateFormat( "yyyy-MM-dd HH:mm:ss" ).create();

    private static final String SOLR_TAG = "tags", SOLR_MACHINE_TAGS = "machine_tags", SOLR_ALL_TAGS = "all_tags";
    private static final String[] solrFields = new String[]{ "id", "title", "description", SOLR_TAG,
            SOLR_MACHINE_TAGS };

    private LinkedTreeMap map;


    public JsonContainer( String json ){
        map = GSON.fromJson( json, LinkedTreeMap.class );
    }


    public Document toMongoDoc(){
        Document d = new Document( "_id", map.get( "id" ) );
        d.putAll( map );
        d.remove( "id" );
        return d;
    }


    public SolrInputDocument toSolrInputDoc(){

        SolrInputDocument document = new SolrInputDocument();

        for( String key : solrFields ){
            Object value = map.get( key );
            if( key.equals( "machine_tags" ) && ( ( List ) value ).size() == 0 ) continue;
            document.addField( key, value );
        }//end for

        document.addField( SOLR_ALL_TAGS, getAllTags() );

        return document;
    }


    public List<String> getAllTags(){
        List<String> all_tags = new ArrayList<>();

        if( map.containsKey( SOLR_TAG ) ){
            all_tags.addAll( ( List<String> ) map.get( SOLR_TAG ) );
        }
        if( map.containsKey( SOLR_MACHINE_TAGS ) ){
            all_tags.addAll( ( List<String> ) map.get( SOLR_MACHINE_TAGS ) );
        }

        return all_tags;
    }


    @Override
    public String toString(){
        return map.toString();
    }


    public static void main( String[] args ){
        String json = "{\"id\":\"2409783581\",\"owner\":\"11149526@N08\",\"secret\":\"1b332bc6fe\"," +
                "\"Server\":\"2335\",\"farm\":3,\"title\":\"Moustache\",\"description\":\"P'tain, mon bac à " +
                "fleurs!!\",\"tags\":[\"chat\",\"appart\"],\"machine_tags\":[],\"url\":\"https://farm3.staticflickr" +
                ".com/2335/2409783581_07413b3ca5_o.jpg\",\"height\":3008,\"width\":2008,\"geo\":{\"lat\":0," +
                "\"long\":0,\"accuracy\":0,\"context\":0},\"dates\":{\"upload\":\"2008-04-13 17:14:27+02:0\"," +
                "\"taken\":\"2008-04-13 16:09:12\"}}";

        JsonContainer c = new JsonContainer( json );

        System.out.println( c );
    }//end main

}//end class
