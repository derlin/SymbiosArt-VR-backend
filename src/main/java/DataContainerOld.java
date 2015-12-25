import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Arrays;
import java.util.Map;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */



public class DataContainerOld{

    public static final Gson GSON = new GsonBuilder().setDateFormat( "yyyy-MM-dd HH:mm:ss" ).create();

    @org.apache.solr.client.solrj.beans.Field
    public String id;
    @org.apache.solr.client.solrj.beans.Field
    public String title;
    @org.apache.solr.client.solrj.beans.Field
    public String description;
    @org.apache.solr.client.solrj.beans.Field
    public String[] tags, machine_tags;


    public String owner, secret;

    @SerializedName( "Server" )
    public String server;

    public int farm;

    public String url;

    public int height, width;

    public Map<String, Integer> geo;

    public Map<String, Date> dates;


    public static void main( String[] args ){
        String json = "{\"id\":\"2409783581\",\"owner\":\"11149526@N08\",\"secret\":\"1b332bc6fe\"," +
                "\"Server\":\"2335\",\"farm\":3,\"title\":\"Moustache\",\"description\":\"P'tain, mon bac à " +
                "fleurs!!\",\"tags\":[\"chat\",\"appart\"],\"machine_tags\":[],\"url\":\"https://farm3.staticflickr" +
                ".com/2335/2409783581_07413b3ca5_o.jpg\",\"height\":3008,\"width\":2008,\"geo\":{\"lat\":0," +
                "\"long\":0,\"accuracy\":0,\"context\":0},\"dates\":{\"upload\":\"2008-04-13 17:14:27+02:0\"," +
                "\"taken\":\"2008-04-13 16:09:12\"}}";

        DataContainerOld d = DataContainerOld.fromJson( json );
        LinkedTreeMap map = GSON.fromJson( json, LinkedTreeMap.class );
        System.out.println( d.dump() );
    }//end main


    public String dump(){
        StringBuilder builder = new StringBuilder();
        for( Field field : DataContainerOld.class.getDeclaredFields() ){
            builder.append( field.getName() ).append( ": " );
            Object val;
            try{
                val = field.get( this );
                if( val instanceof String[] ){
                    builder.append( Arrays.toString( ( String[] ) val ) );
                }else{
                    builder.append( val.toString() );
                }
            }catch( Exception e ){
                builder.append( "??" );
            }
            builder.append( "\n" );
        }//end for
        return builder.toString();
    }


    @Override
    public String toString(){
        return "DataContainer:" + id;
    }


    public static DataContainerOld fromJson( String json ){
        return GSON.fromJson( json, DataContainerOld.class );
    }
}//end class
