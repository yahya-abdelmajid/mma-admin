package mma.admin.domains.interfaces.repository;

import mma.admin.domains.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface UsersRepository{
    List<User> getUsers();
    boolean exists(String id);
    User getUser(String id);
}
