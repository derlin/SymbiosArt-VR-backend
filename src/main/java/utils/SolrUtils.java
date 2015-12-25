package utils;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */
public class SolrUtils{

    public final static String HOST = "dulcolax.local";
    public final static int PORT = 8983;
    public final static String DEFAULT_CORE = "core0";


    // ----------------------------------------------------


    public static void main( String[] args ){
        createClient();
    }//end main

    public static SolrClient createClient(){
        return new HttpSolrClient( getConnectionString( DEFAULT_CORE ) );
    }

    public static SolrClient createClient(String core){
        return new HttpSolrClient( getConnectionString(core) );
    }


    // ----------------------------------------------------

    public static String getConnectionString(String core){
        return String.format( "http://%s:%d/solr/%s", HOST, PORT, core );
    }



}//end class
