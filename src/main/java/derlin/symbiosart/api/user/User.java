package derlin.symbiosart.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.commons.TagsVector;
import org.bson.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * @author: Lucy Linder
 * @date: 04.01.2016
 */
@XmlRootElement
public class User implements Interfaces.IMongoInsertable{

    public static final String MONGO_NAME_KEY = "_id";
    public static final String MONGO_TAGS_KEY = "tags_vector";

    private String name;

    private TagsVector tagsVector;


    @JsonProperty( "name" )
    public String getName(){
        return name;
    }


    @JsonProperty( "name" )
    public void setName( String name ){
        this.name = name;
    }


    @JsonProperty( "tags_vector" )
    public TagsVector getTagsVector(){
        return tagsVector;
    }


    @JsonProperty( "tags_vector" )
    public void setTagsVector( TagsVector tagsVector ){
        this.tagsVector = tagsVector;
    }

    // ----------------------------------------------------


    public Document toMongoDoc(){
        Document doc = new Document();
        doc.put( MONGO_NAME_KEY, name );
        doc.put( MONGO_TAGS_KEY, tagsVector );
        return doc;
    }


    public static User fromMongoDoc( Document doc ){
        if( !doc.containsKey( MONGO_NAME_KEY ) ) return null;
        User user = new User();
        user.setName( doc.getString( MONGO_NAME_KEY ) );
        if( doc.containsKey( MONGO_TAGS_KEY ) ){
            try{
                user.tagsVector = new TagsVector( ( Map<String, Integer> ) doc.get( MONGO_TAGS_KEY ) );
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
        return user;
    }

}//end class
