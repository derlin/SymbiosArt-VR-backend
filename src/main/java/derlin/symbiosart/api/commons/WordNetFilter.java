package derlin.symbiosart.api.commons;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: Lucy Linder
 * @date: 18.01.2016
 */
public class WordNetFilter{

    private static IDictionary wordNetDict;
    private static final String WORD_NET_PATH = "/home/lucy/git/WordNet/dict";
    private static final POS[] POSES = new POS[]{ POS.NOUN, POS.ADJECTIVE, POS.ADVERB, POS.VERB };


    public WordNetFilter() throws Exception{
        if( wordNetDict == null ) init();
    }


    public boolean isWord( String s ){
        for( POS pose : POSES ){
            if( wordNetDict.getIndexWord( s, pose ) != null ) return true;
        }//end for
        return false;
    }


    public List<String> filterTags( List<String> tags ){

        List<String> newlist = tags.stream()//
                .map( s -> this.isWord( s ) ? s : null ) //
                .filter( s -> s != null ) //
                .collect( Collectors.toList() );

        return newlist;

    }


    private static void init() throws Exception{
        URL url = new URL( "file", null, WORD_NET_PATH );
        wordNetDict = new Dictionary( url );
        wordNetDict.open();
    }


    // ----------------------------------------------------


    private static final String JSON_FILE = "/media/lucy/Elements/MY_FLICKR_IMAGES/metas.json";


    public static void main( String[] args ) throws Exception{
        WordNetFilter f = new WordNetFilter();
        // index all from file, return the number of lines actually added
        try( Stream<String> stream = Files.lines( Paths.get( JSON_FILE ) ) ){
            stream //
                    .map( Document::parse )  //
                    .map( d -> ( List<String> ) d.get( "tags" ) ) //
                    .map(ts -> {System.out.print(ts + " => "); return ts; }) //
                    .map( f::filterTags ) //
                    .forEach( System.out::println );

        }catch( IOException e ){
            e.printStackTrace();
        }
    }//end main

}//end class
