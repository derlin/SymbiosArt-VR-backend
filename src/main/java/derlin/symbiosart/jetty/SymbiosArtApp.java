package derlin.symbiosart.jetty;

/**
 * @author: Lucy Linder
 * @date: 27.12.2015
 */

import org.glassfish.jersey.server.ResourceConfig;

public class SymbiosArtApp extends ResourceConfig{

    public SymbiosArtApp(){
        packages( "derlin.symbiosart" );
    }
}

