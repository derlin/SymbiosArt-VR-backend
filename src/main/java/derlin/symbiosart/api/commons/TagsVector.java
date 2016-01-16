package derlin.symbiosart.api.commons;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * A simple map tagname/wheight, supporting
 * json and xml serialization.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 27.12.2015
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
@XmlRootElement
public class TagsVector extends TreeMap<String, Integer>{

    public TagsVector(){
    }


    public TagsVector( Map<String, Integer> m ){
        super( m );
    }


    public String toPrettyString(){
        return entrySet().stream()  //
                .map( e -> String.format( "%s=%s", e.getKey(), e.getValue() ) ) //
                .collect( Collectors.joining( ", " ) );
    }

    /* *****************************************************************
     * only for xml serialization
     * ****************************************************************/


    @XmlElement( name = "tag" )
    public List<Tag> getForXmlSerialization(){
        return this.entrySet().stream()//
                .map( k -> new Tag( k.getKey(), k.getValue() ) ) //
                .collect( Collectors.toList() );
    }


    public static class Tag{
        private String value;
        private int weight;


        public Tag( String value, int weight ){
            this.value = value;
            this.weight = weight;
        }


        public String getValue(){
            return value;
        }


        public void setValue( String value ){
            this.value = value;
        }


        public int getWeight(){
            return weight;
        }


        public void setWeight( int weight ){
            this.weight = weight;
        }
    }

}//end class
