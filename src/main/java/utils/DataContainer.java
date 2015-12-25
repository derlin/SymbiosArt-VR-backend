package utils;

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


public class DataContainer{

    public static final Gson GSON = new GsonBuilder().setDateFormat( "yyyy-MM-dd HH:mm:ss" ).create();

    private static final String TAGS_KEY = "tags";
    private static final String TAGS_COUNT_KEY = "t_count";
    private static final String MACHINE_TAGS_KEY = "machine_tags";
    private static final String SOLR_ALL_TAGS_KEY = "all_tags";
    private static final String[] solrFields = new String[]{ "id", "title", "description", TAGS_KEY, MACHINE_TAGS_KEY };

    private LinkedTreeMap map;


    /**
     * Create a data container from a valid json string
     * example:<br />
     * <pre><code>
     * {
     * "id":"2932067831",
     * "owner":"16041363@N00",
     * "secret":"8885c3d53f",
     * "Server":"3026",
     * "farm":4,
     * "title":"spot the intrepid explorer",
     * "description":"",
     * "tags":["utah","ut"],
     * "machine_tags":['sea'],
     * "url":"https://farm4.staticflickr.com/3026/2932067831_93a741afbb_o.jpg",
     * "height":2602,
     * "width":3906,
     * "geo":{ "lat":0,"long":0, "accuracy":16, "context":0 },
     * "dates":{ "upload":"2008-10-12 00:41:15+02:0", "taken":"2008-09-29 15:12:28" }
     * }
     * </code></pre>
     *
     * @param json the json string
     */
    public DataContainer( String json ){
        map = GSON.fromJson( json, LinkedTreeMap.class );
    }


    /**
     * Create a mongoDB document for insert.
     *
     * @return the mongo doc
     */
    public Document toMongoDoc(){
        Document d = new Document( "_id", map.get( "id" ) );
        d.putAll( map );
        d.remove( "id" );
        return d;
    }


    /**
     * Create a solr document for insert.
     *
     * @return the document
     */
    public SolrInputDocument toSolrInputDoc(){

        SolrInputDocument document = new SolrInputDocument();

        for( String key : solrFields ){
            Object value = map.get( key );
            if( key.equals( "machine_tags" ) ) continue;
            document.addField( key, value );
        }//end for

        Object tags = map.get( "tags" );
        document.addField( TAGS_COUNT_KEY, ( ( List ) tags ).size() );

        //document.addField( SOLR_ALL_TAGS_KEY, getAllTags() );

        return document;
    }


    /**
     * Create a solr document for insert.
     *
     * @return the document
     */
    public SolrInputDocument toSolrInputDoc0(){

        SolrInputDocument document = new SolrInputDocument();

        for( String key : solrFields ){
            Object value = map.get( key );
            if( key.equals( "machine_tags" ) && ( ( List ) value ).size() == 0 ) continue;
            document.addField( key, value );
        }//end for

        document.addField( SOLR_ALL_TAGS_KEY, getAllTags() );

        return document;
    }


    /**
     * Check that the json contains at least an id and some tags
     *
     * @return
     */
    public boolean validate(){
        return map.containsKey( "id" ) && map.get( "id" ).toString().length() > 2 && //
                map.containsKey( TAGS_KEY ) &&  //
                ( ( List<String> ) map.get( TAGS_KEY ) ).size() > 0;
    }


    /**
     * return an arraylist of all the tags (tags + machine tags)
     * This info must be present in the solr input doc under the key #SOLR_ALL_TAGS_KEY
     *
     * @return the list of all tags
     */
    public List<String> getAllTags(){
        List<String> all_tags = new ArrayList<>();

        if( map.containsKey( TAGS_KEY ) ){
            all_tags.addAll( ( List<String> ) map.get( TAGS_KEY ) );
        }
        if( map.containsKey( MACHINE_TAGS_KEY ) ){
            all_tags.addAll( ( List<String> ) map.get( MACHINE_TAGS_KEY ) );
        }

        return all_tags;
    }


    @Override
    public String toString(){
        return map.toString();
    }

    // ----------------------------------------------------


    public static void main( String[] args ){
        String json = "{\"id\":\"2409783581\",\"owner\":\"11149526@N08\",\"secret\":\"1b332bc6fe\"," +
                "\"Server\":\"2335\",\"farm\":3,\"title\":\"Moustache\",\"description\":\"P'tain, mon bac à " +
                "fleurs!!\",\"tags\":[\"chat\",\"appart\"],\"machine_tags\":[],\"url\":\"https://farm3.staticflickr" +
                ".com/2335/2409783581_07413b3ca5_o.jpg\",\"height\":3008,\"width\":2008,\"geo\":{\"lat\":0," +
                "\"long\":0,\"accuracy\":0,\"context\":0},\"dates\":{\"upload\":\"2008-04-13 17:14:27+02:0\"," +
                "\"taken\":\"2008-04-13 16:09:12\"}}";

        DataContainer c = new DataContainer( json );

        System.out.println( c );
    }//end main

}//end class
