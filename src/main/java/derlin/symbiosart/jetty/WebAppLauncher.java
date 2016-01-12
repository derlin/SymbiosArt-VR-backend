package derlin.symbiosart.jetty;

import derlin.symbiosart.api.commons.Constants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * This class launches the Jetty Server with
 * exactly the same configuration as if the server
 * had been launched from maven (mvn jetty:run)
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
public class WebAppLauncher{

    public static void main( String[] args ) throws Exception{

        Server server = new Server( Constants.SERVER_PORT );

        WebAppContext context = new WebAppContext();
        context.setParentLoaderPriority( true );
        context.setDescriptor( WebAppLauncher.class.getResource( "/WEB-INF/web.xml" ).toString() );
        context.setResourceBase( WebAppLauncher.class.getResource( "/" ).getPath() );
        context.setContextPath( "/" );
        context.setParentLoaderPriority( true );

        server.setHandler( context );

        server.start();
        server.join();
    }
}
