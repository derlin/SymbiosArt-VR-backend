package derlin.symbiosart.api.commons;

import derlin.symbiosart.api.commons.exceptions.NotFoundException;
import derlin.symbiosart.api.commons.exceptions.UnexpectedError;
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
        /**
         * get a list of {@param nbr} images based on the tags vector preferences.
         *
         * @param tagsVector the user profile, a map {@code <tag name, weight>}
         * @param nbr        the number of results to return
         * @return a list of bson documents
         * @throws UnexpectedError if an error occurs during the query
         */
        List<Document> getSuggestions( TagsVector tagsVector, int nbr ) throws UnexpectedError;

        /**
         * Get all the details availables for the image with id {@param id}.
         *
         * @param id the id of the image
         * @return the bson document with all the details
         * @throws NotFoundException if the id does not match any document.
         */
        Document getDetails( String id ) throws NotFoundException;
    }

    /**
     * The user api, implementing
     * the CRUD operations.
     */
    public interface IUsersApi{
        /**
         * Get a list of user ids and names.
         *
         * @return bson documents, each containing "_id" and "name" entries.
         */
        List<Document> getUsers();

        /**
         * get the specified user.
         *
         * @param id the id of the user
         * @return a bson document with the user details
         * @throws NotFoundException if the id does not match any document.
         */
        User getUser( String id ) throws NotFoundException;

        /**
         * add a new user
         *
         * @param user the new user. The _id will be generated automatically.
         */
        void addUser( User user );

        /**
         * update the given user
         *
         * @param id   the id of the user to update
         * @param user the new fields
         * @return true if the update was successful.
         */
        boolean updateUser( String id, User user );

        /**
         * remove the given user
         *
         * @param id the user id to delete.
         */
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
