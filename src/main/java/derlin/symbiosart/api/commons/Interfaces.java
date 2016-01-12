package derlin.symbiosart.api.commons;

import derlin.symbiosart.api.user.User;
import org.apache.solr.common.SolrInputDocument;
import org.bson.Document;

import java.util.List;

/**
 * @author: Lucy Linder
 * @date: 05.01.2016
 */
public class Interfaces{

    @FunctionalInterface
    public interface IMongoInsertable{
        Document toMongoDoc();
    }

    @FunctionalInterface
    public interface ISolrIndexable{
        SolrInputDocument toSolrInputDoc();
    }

    // ----------------------------------------------------

    public interface IIMagesApi{
        List<Document> getSuggestions( TagsVector tagsVector, int nbr );
    }

    // ----------------------------------------------------

    public interface IUsersApi{
        List<Document> getUsers();

        User getUser( String id );

        void addUser( User user );

        boolean updateUser( String id, User user );

        void removeUser( String id );
    }

    // ----------------------------------------------------

    public interface IApiProvider{
        IIMagesApi getImagesApi();

        IUsersApi getUsersApi();
    }
}//end class
