import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;

import java.io.IOException;

/**
 * @author: Lucy Linder
 * @date: 19.12.2015
 */
public class SolrConnector{
    public static void main( String[] args ){
        String urlString = "http://localhost:8983/solr/core0";
        SolrClient solr = new HttpSolrClient( urlString );


        String json = "{\"id\":\"2409783581\",\"owner\":\"11149526@N08\",\"secret\":\"1b332bc6fe\"," +
                "\"Server\":\"2335\",\"farm\":3,\"title\":\"Moustache\",\"description\":\"P'tain, mon bac à " +
                "fleurs!!\",\"tags\":[\"chat\",\"appart\"],\"machine_tags\":[],\"url\":\"https://farm3.staticflickr" +
                ".com/2335/2409783581_07413b3ca5_o.jpg\",\"height\":3008,\"width\":2008,\"geo\":{\"lat\":0," +
                "\"long\":0,\"accuracy\":0,\"context\":0},\"dates\":{\"upload\":\"2008-04-13 17:14:27+02:0\"," +
                "\"taken\":\"2008-04-13 16:09:12\"}}";

        JsonContainer d = new JsonContainer( json );



        try{
            UpdateResponse response = solr.add(d.toSolrInputDoc());
            System.out.println(response);
            solr.commit();
        }catch( SolrServerException | IOException e ){
            e.printStackTrace();
        }
    }//end main



}//end class
