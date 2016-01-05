package derlin.symbiosart.flickr;

import org.bson.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @author: Lucy Linder
 * @date: 05.01.2016
 */
public class BatchDownloader{

    private static final int DEFAULT_RESULTS = 100;
    private File imgDir, metaDir;


    public static void main( String[] args ){
        if( args.length < 2 ) exitErr( "usage: targetDir tag[,tag]" );

        File dir = new File( args[ 0 ] );
        if( !dir.exists() && !dir.mkdir() ) exitErr( "Could not create dir " + dir.getPath() );

        String[] tags = args[ 1 ].split( "," );

        int results = DEFAULT_RESULTS;
        if( args.length > 2 ){
            results = Integer.parseInt( args[ 2 ] );
        }

        go( dir, tags, results );

    }//end main


    public static void go( File dir, String[] tags, int results ){
        BatchDownloader downloader = new BatchDownloader( dir, dir );
        List<Document> list = FlickrUtils.search( tags, results );
        System.out.println( "FLICKR : " + list.size() + " results." );

        int nbr = list.parallelStream()   //
                .filter( d -> ( ( List ) d.get( "tags" ) ).size() > tags.length ) //
                .map( downloader::save )  //
                .mapToInt( ok -> ok ? 1 : 0 )  //
                .sum();

        System.out.println( nbr + " saved." );
    }


    public BatchDownloader( File imgDir, File metaDir ){
        this.imgDir = imgDir;
        this.metaDir = metaDir;
    }


    private boolean save( Document doc ){
        boolean ok = saveImage( doc ) && saveMetas( doc );
        System.out.println( doc.getString( "_id" ) + ": " + ( ok ? "OK" : "ERROR" ) );
        return ok;
    }


    private boolean saveMetas( Document doc ){
        String filename = doc.get( "_id" ) + ".json";
        try{
            Files.write( Paths.get( metaDir.getPath(), filename ), doc.toJson().getBytes(), StandardOpenOption
                    .CREATE_NEW );
            return true;
        }catch( IOException e ){
            System.err.println( "could not save " + filename + " : " + e );
            return false;
        }
    }


    private boolean saveImage( Document doc ){
        String stringurl = doc.getString( "url" );

        try{
            String format = stringurl.substring( stringurl.length() - 3 );
            String filename = doc.get( "_id" ) + "." + format;
            File file = new File( imgDir, filename );
            if( file.exists() ) return true; // do nothing if already exists

            URL url = new URL( stringurl );
            BufferedImage image = ImageIO.read( url );
            ImageIO.write( image, format, new File( imgDir, filename ) );
            return true;

        }catch( IOException e ){
            System.err.println( "could not save " + stringurl + " : " + e );
            return false;
        }
    }


    public static void exitErr( String msg ){
        System.err.println( msg );
        System.exit( 1 );
    }
}//end class
