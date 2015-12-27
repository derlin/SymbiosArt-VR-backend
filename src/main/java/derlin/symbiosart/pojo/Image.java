package derlin.symbiosart.pojo;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Lucy Linder
 * @date: 27.12.2015
 */
public class Image{

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

}//end class
