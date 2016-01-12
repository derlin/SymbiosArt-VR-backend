package derlin.symbiosart.jetty;

/**
 * Configuration class for the Jetty Server
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
import org.glassfish.jersey.server.ResourceConfig;

public class SymbiosArtApp extends ResourceConfig{

    public SymbiosArtApp(){
        packages( "derlin.symbiosart" );
    }
}

