package derlin.symbiosart.utils;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */
public class SolrUtils{

    public final static String HOST = "dulcolax.local";
    public final static int PORT = 8983;


    // ----------------------------------------------------


    public static void main( String[] args ){
        createClient("core0");
    }//end main


    public static SolrClient createClient( String core ){
        return new HttpSolrClient( getConnectionString( core ) );
    }


    // ----------------------------------------------------


    public static String getConnectionString( String core ){
        return String.format( "http://%s:%d/solr/%s", HOST, PORT, core );
    }


}//end class
