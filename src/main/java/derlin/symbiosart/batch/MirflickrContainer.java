package derlin.symbiosart.batch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.solr.common.SolrInputDocument;
import org.bson.Document;

import java.util.List;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */


public class MirflickrContainer implements IDataContainer{

    public static final Gson GSON = new GsonBuilder().setDateFormat( "yyyy-MM-dd HH:mm:ss" ).create();

    private static final String TAGS_KEY = "tags";
    private static final String TAGS_COUNT_KEY = "tags_count";
    private static final String ID_KEY = "id";
    private static final String OWNER_KEY = "owner";
    private static final String[] solrFields = new String[]{ ID_KEY, OWNER_KEY, TAGS_KEY, TAGS_COUNT_KEY };

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
    public MirflickrContainer( String json ){
        map = GSON.fromJson( json, LinkedTreeMap.class );
        // convert id to int
        Object o = map.get( ID_KEY );
        map.put( ID_KEY, ( ( int ) ( double ) o ) );
    }


    /**
     * Create a mongoDB document for insert.
     *
     * @return the mongo doc
     */
    public Document toMongoDoc(){
        Document d = new Document( "_id", map.get( "id" ).toString() );
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
            document.addField( key, value );
        }//end for


        Object tags = map.get( "tags" );
        document.addField( TAGS_COUNT_KEY, ( ( List ) tags ).size() );

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


    @Override
    public String toString(){
        return map.toString();
    }

    // ----------------------------------------------------


    public static void main( String[] args ){
        String json = "{\"id\":7,\"tags\":[\"bianconero\",\"blackwhite\",\"bw\",\"perfotoup\",\"danaefestival\"," +
                "\"habilléd’eau–camera\",\"girl\",\"spogliarsi\"," +
                "\"fotodiunpoditempofadicuimierodimenticatalesistenzamainquestoperiodosonounpoincasinataperscattare" +
                "\"],\"owner\":\"74741809@N00\"}";

        MirflickrContainer c = new MirflickrContainer( json );

        System.out.println( c );
    }//end main

}//end class
