package derlin.symbiosart.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.commons.TagsVector;
import org.bson.Document;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;


/**
 * This class represents a user. It supports conversion
 * from/to mongo documents and xml/json serialization.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
@XmlRootElement
public class User implements Interfaces.IMongoInsertable{

    public static final String MONGO_NAME_KEY = "name";
    public static final String MONGO_TAGS_KEY = "tags_vector";

    private String id, name;

    private TagsVector tagsVector;

    private List<String> likedIds, dislikedIds;


    @JsonProperty( "_id" )
    public String getId(){
        return id;
    }


    @JsonProperty( "_id" )
    public void setId( String id ){
        this.id = id;
    }


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


    @JsonProperty( "liked_ids" )
    public List<String> getLikedIds(){
        return likedIds;
    }


    @JsonProperty( "liked_ids" )
    public void setLikedIds( List<String> likedIds ){
        this.likedIds = likedIds;
    }


    @JsonProperty( "disliked_ids" )
    public List<String> getDislikedIds(){
        return dislikedIds;
    }


    @JsonProperty( "disliked_ids" )
    public void setDislikedIds( List<String> dislikedIds ){
        this.dislikedIds = dislikedIds;
    }

    // ----------------------------------------------------


    public Document toMongoDoc(){
        Document doc = new Document();

        doc.put( MONGO_NAME_KEY, name );
        doc.put( MONGO_TAGS_KEY, tagsVector );

        if( id != null ) doc.put( "_id", id );
        if( likedIds != null ) doc.put( "liked_ids", likedIds );
        if( dislikedIds != null ) doc.put( "disliked_ids", dislikedIds );

        return doc;
    }


    public static User fromMongoDoc( Document doc ){
        if( !doc.containsKey( "_id" ) || !doc.containsKey( MONGO_NAME_KEY ) ) return null;
        User user = new User();
        user.setId( doc.getString( "_id" ) );
        user.setName( doc.getString( MONGO_NAME_KEY ) );

        if( doc.containsKey( MONGO_TAGS_KEY ) ){
            try{
                user.tagsVector = new TagsVector( ( Map<String, Integer> ) doc.get( MONGO_TAGS_KEY ) );
            }catch( Exception e ){
                e.printStackTrace();
            }
        }

        if( doc.containsKey( "liked_ids" ) ) user.setLikedIds( ( List<String> ) doc.get( "liked_ids" ) );
        if( doc.containsKey( "disliked_ids" ) ) user.setDislikedIds( ( List<String> ) doc.get( "disliked_ids" ) );
        return user;
    }


    @Override
    public String toString(){
        return String.format( "[%s] %s (tv:%d)", id, name, tagsVector.size() );
    }
}//end class
