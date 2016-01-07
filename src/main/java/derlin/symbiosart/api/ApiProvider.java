package derlin.symbiosart.api;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import derlin.symbiosart.api.commons.Constants;
import derlin.symbiosart.api.commons.Interfaces;
import derlin.symbiosart.api.img.ImagesApi;
import derlin.symbiosart.api.user.UsersApi;

/**
 * @author: Lucy Linder
 * @date: 06.01.2016
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

    // ----------------------------------------------------


    public void setMongoImagesColl( String mongoImagesColl ){
        this.mongoImagesColl = mongoImagesColl;
    }


    public void setMongoUsersColl( String mongoUsersColl ){
        this.mongoUsersColl = mongoUsersColl;
    }


    public void setSolrCore( String solrCore ){
        this.solrCore = solrCore;
    }

    // ----------------------------------------------------


    @Override
    public Interfaces.IIMagesApi getImagesApi(){
        return new ImagesApi(  //
                Constants.SOLR_CLIENT_CREATOR.apply( solrCore ), //
                mongoDb.getCollection( mongoImagesColl ) );
    }


    @Override
    public Interfaces.IUsersApi getUsersApi(){
        return new UsersApi(mongoDb.getCollection( mongoUsersColl ));
    }

}
