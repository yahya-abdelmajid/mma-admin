package mma.admin.domains.interfaces;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.repository.UsersRepository;

import java.util.Set;
import java.util.stream.Stream;

public interface UsersService extends UsersRepository {
    Set<User> getUsers();
    boolean exists(String id);
    User getUser(String id);

}
