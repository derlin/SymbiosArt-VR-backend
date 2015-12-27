package derlin.symbiosart.utils;

/**
 * @author: Lucy Linder
 * @date: 25.12.2015
 */
public interface IDataContainer{
    public org.apache.solr.common.SolrInputDocument toSolrInputDoc();

    public org.bson.Document toMongoDoc();

}//end interface
