package sk.ygor.dbtransactions.snippets;

public class UserService extends AbstractService {

    public void createNewUser(String username) {
        getDatabaseSession().insertObject(new User(username));
    }
}
