package derlin.symbiosart.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author: Lucy Linder
 * @date: 19.02.2016
 */
public class Config{

    private static final Logger LOG = LoggerFactory.getLogger( ConfigOrig.class );
    private static final String CONF_FILE = "/symbiosart.properties";
    private static Properties properties;


    static{
        load( CONF_FILE );
    }


    // ----------------------------------------------------


    private static void load( String file ){
        properties = new Properties();
        try{
            URL configFile = Config.class.getResource( file );
            if( configFile != null ){
                properties.load( configFile.openStream() );
                LOG.info( "Config file loaded." );
            }else{
                LOG.info( "No config file." );
            }
        }catch( IOException ex ){

            LOG.error( "Cannot load config file {}", file );
        }
    }

    // ----------------------------------------------------


    public static String getProperty( String key ){
        return properties == null ? null : properties.getProperty( key );
    }


    public static String getProperty( String key, String def ){
        String value = getProperty( key );
        if( value == null ){
            return def;
        }else{
            return value;
        }
    }


    public static Integer getProperty( String key, Integer def ){
        try{
            String raw = getProperty( key, String.valueOf( def ) );
            return raw == null ? null : Integer.parseInt( raw );
        }catch( NumberFormatException ex ){
            return def;
        }
    }
}//end class
