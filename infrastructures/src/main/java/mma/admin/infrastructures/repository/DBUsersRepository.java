package mma.admin.infrastructures.repository;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.repository.UsersRepository;
import mma.admin.domains.interfaces.service.UserService;
import mma.admin.infrastructures.service.DBUserService;

import java.util.List;

public class DBUsersRepository implements UsersRepository {

    private final List<User> users;

    public DBUsersRepository(UserService DBUserService) {
        users = DBUserService.getAllUsers();
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUser(String id){
        return users.stream().filter(user -> user.badge().equals(id)).findFirst().get();
    }

    @Override
    public boolean exists(String badge){
        return users.stream().anyMatch(user -> user.badge().equals(badge));
    }

}
