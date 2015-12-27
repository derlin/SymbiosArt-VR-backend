package derlin.symbiosart.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebAppLauncher{

    public static final int PORT = 8680;


    public static void main( String[] args ) throws Exception{

        Server server = new Server( PORT );

        WebAppContext context = new WebAppContext();
        context.setDescriptor( WebAppLauncher.class.getResource( "/WEB-INF/web.xml" ).toString() );
        context.setResourceBase( WebAppLauncher.class.getResource( "/" ).getPath() );
        context.setContextPath( "/" );
        context.setParentLoaderPriority( true );

        server.setHandler( context );

        server.start();
        server.join();
    }
}
