package derlin.symbiosart.api.user;

import derlin.symbiosart.Constants;
import derlin.symbiosart.api.commons.TagsVector;
import org.junit.After;
import org.junit.Before;

/**
 * @author: Lucy Linder
 * @date: 05.01.2016
 */
public class UserApiTest{

    private static final String ID = "junit", ID2 = "test";
    User user = new User();
    UsersApi api = new UsersApi( Constants.MONGO_COLL_CREATOR.apply( "users_test" ) );

    @Before
    public void setup(){
        user.setName( ID );
        TagsVector tags = new TagsVector();
        tags.put( "j", 1 );
        tags.put( "unit", 1 );
        user.setTagsVector( tags );
    }

    @After
    public void cleanup(){
        api.removeUser( ID );
        api.removeUser( ID2 );
    }

    /*
    @org.junit.Test
    public void testAll(){
        // add
        api.addUser( user );
        List<String> users = api.getUsers();
        Assert.assertTrue( users.contains( ID ) );

        // get
        User u = api.getUser( ID );
        Assert.assertEquals( user.getTagsVector(), u.getTagsVector() );

        // update tags
        user.getTagsVector().put( "j", 10 );
        api.updateUser( ID, user );
        u = api.getUser( ID );
        Assert.assertEquals( 10, u.getTagsVector().get( "j" ).intValue() );

        // update id
        user.setName( ID2 );
        api.updateUser( ID, user );
        users = api.getUsers();
        Assert.assertFalse( users.contains( ID ) );
        Assert.assertTrue( users.contains( ID2 ) );

        // remove
        api.removeUser( ID2 );
        users = api.getUsers();
        Assert.assertFalse( users.contains( ID2 ) );
    }     */


}//end class
