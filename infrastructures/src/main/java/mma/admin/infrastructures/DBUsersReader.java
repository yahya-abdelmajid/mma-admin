package mma.admin.infrastructures;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.UsersReader;
import mma.admin.infrastructures.service.UserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBUsersReader implements UsersReader {
    private UserService userService;
    private List<User> users;

    public DBUsersReader(UserService userService ) {
        this.userService = userService;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    //généré par une IA
    @Override
    public void readAllUsers(){
        this.users= userService.getAllUsers();
    }
}
