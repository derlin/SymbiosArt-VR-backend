package derlin.symbiosart.api.commons;

import derlin.symbiosart.api.user.User;
import org.apache.solr.common.SolrInputDocument;
import org.bson.Document;

import java.util.List;

/**
 * This class defines the interfaces of/sed by the two
 * main APIs (image and user).
 * <p>
 * This is important in order to decouple the API
 * providers and the jetty server/rest services.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 05.01.2016
 * ---------------------------------------------------
 *
 * @author: Lucy Linder
 */
public class Interfaces{


    // ----------------------------------------------------

    /**
     * Api provider, used by the rest service.
     * Its role is to instantiate the two apis
     * with the corect connection strings
     */
    public interface IApiProvider{
        IIMagesApi getImagesApi();

        IUsersApi getUsersApi();
    }

    /**
     * The image api, able to suggest
     * image metas based on a tag vector.
     */
    public interface IIMagesApi{
        List<Document> getSuggestions( TagsVector tagsVector, int nbr );

        Document getDetails(String id);
    }

    /**
     * The user api, implementing
     * the CRUD operations.
     */
    public interface IUsersApi{
        List<Document> getUsers();

        User getUser( String id );

        void addUser( User user );

        boolean updateUser( String id, User user );

        void removeUser( String id );
    }


    // ---------------------------------------------------

    /**
     * Useful for tests and batch utils.
     */
    @FunctionalInterface
    public interface IMongoInsertable{
        Document toMongoDoc();
    }

    @FunctionalInterface
    public interface ISolrIndexable{
        SolrInputDocument toSolrInputDoc();
    }
}//end class
