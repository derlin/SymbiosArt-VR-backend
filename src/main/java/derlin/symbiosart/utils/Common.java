package derlin.symbiosart.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author: Lucy Linder
 * @date: 26.12.2015
 */
public class Common{

    @XmlRootElement
    public static class TagsVector extends TreeMap<String, Integer>{

        @XmlElement( name = "tag" )
        public List<Tag> getForXmlSerialization(){
            return this.entrySet().stream()//
                    .map( k -> new Tag( k.getKey(), k.getValue() ) ) //
                    .collect( Collectors.toList() );
        }
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

    /* *****************************************************************
     *
     * ****************************************************************/

    @XmlRootElement
    public static class Image{
        private String id;
        private String[] tags;


        public Image(){}


        public Image( String id, String[] tags ){
            this.id = id;
            this.tags = tags;
        }


        public Image( String id, List<String> tags ){
            this.id = id;
            this.tags = tags.toArray( new String[]{} );
        }


        public String getUrl(){
            return null;
        }


        public String getId(){
            return id;
        }


        public void setId( String id ){
            this.id = id;
        }


        public String[] getTags(){
            return tags;
        }


        public void setTags( String[] tags ){
            this.tags = tags;
        }


        public void setTags( List<String> tags ){
            this.tags = tags.toArray( new String[]{} );
        }


        @Override
        public String toString(){
            return String.format( "[%s] : %s", id, Arrays.toString( tags ) );
        }
    }
}//end class
