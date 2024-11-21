package mma.admin.infrastructures.repository;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.UsersReader;
import mma.admin.domains.interfaces.repository.UsersRepository;

import java.util.List;
import java.util.Set;

public class DBUsersRepository implements UsersRepository {

    private List<User> users;

    public DBUsersRepository(UsersReader usersReader) {
        users = usersReader.getUsers();
    }


    @Override
    public List<User> getUsers() {
        return users;
    }

    public User getUser(String id){
        return users.stream().filter(user -> user.getBadge().equals(id)).findFirst().get();
    }

    @Override
    public boolean exists(String badge){
        return users.stream().anyMatch(user -> user.getBadge().equals(badge));
    }
}
