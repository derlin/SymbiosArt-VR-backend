package derlin.symbiosart.api;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import derlin.symbiosart.api.commons.Constants;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.img.ImagesApi;
import derlin.symbiosart.api.user.UsersApi;

/**
 * This class is an IApiProvider configured to use
 * the server informations defined in {@link Constants}.
 * It provides instances apis of classes {@link ImagesApi} and
 * {@link UsersApi}.
 * ---------------------------------------------------
 * Context: Projet de Bachelor - SymbiosArt Immersion
 * date 01.01.2016
 * ---------------------------------------------------
 *
 * @author Lucy Linder
 */
public class ApiProvider implements Interfaces.IApiProvider{

    private MongoDatabase mongoDb;
    private String mongoImagesColl = Constants.MONGO_IMAGES_COLL;
    private String mongoUsersColl = Constants.MONGO_USERS_COLL;
    private String solrCore = Constants.SOLR_DEFAULT_CORE;

    // ----------------------------------------------------


    public ApiProvider(){
        mongoDb = new MongoClient( Constants.MONGO_HOST ).getDatabase( Constants.MONGO_DB );
    }


    /* *****************************************************************
     * getters and setters, just in case
     * ****************************************************************/


    public void setMongoImagesColl( String mongoImagesColl ){
        this.mongoImagesColl = mongoImagesColl;
    }


    public void setMongoUsersColl( String mongoUsersColl ){
        this.mongoUsersColl = mongoUsersColl;
    }


    public void setSolrCore( String solrCore ){
        this.solrCore = solrCore;
    }


    /* *****************************************************************
     * implement interface
     * ****************************************************************/


    @Override
    public Interfaces.IIMagesApi getImagesApi(){
        return new ImagesApi(  //
                Constants.SOLR_CLIENT_CREATOR.apply( solrCore ), //
                mongoDb.getCollection( mongoImagesColl ) );
    }


    @Override
    public Interfaces.IUsersApi getUsersApi(){
        return new UsersApi( mongoDb.getCollection( mongoUsersColl ) );
    }

}
