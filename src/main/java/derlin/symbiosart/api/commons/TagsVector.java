package derlin.symbiosart.api.commons;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author: Lucy Linder
 * @date: 27.12.2015
 */
@XmlRootElement
public class TagsVector extends TreeMap<String, Integer>{

    public TagsVector(){
    }


    public TagsVector( Map<String, Integer> m ){
        super( m );
    }


    @XmlElement( name = "tag" )
    public List<Tag> getForXmlSerialization(){
        return this.entrySet().stream()//
                .map( k -> new Tag( k.getKey(), k.getValue() ) ) //
                .collect( Collectors.toList() );
    }


    /* *****************************************************************
     * one tag (mainly for xml serialization)
     * ****************************************************************/
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
