package derlin.symbiosart.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author: Lucy Linder
 * @date: 26.12.2015
 */
public class JettyMain{
    public static void main( String[] args ) throws Exception{

        ServletContextHandler context = new ServletContextHandler( ServletContextHandler.NO_SESSIONS );
        context.setContextPath( "/" );

        Server jettyServer = new Server( 8000 );

        jettyServer.setHandler( context );

        ServletHolder jerseyServlet = context.addServlet( ServletContainer.class, "/*" );
        jerseyServlet.setInitOrder( 1 );
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter( "jersey.config.server.provider.packages", //
                "derlin.symbiosart.jetty.rest;org.codehaus.jackson.jaxrs" );
        jettyServer.start();
        jettyServer.join();
    }

}//end class
