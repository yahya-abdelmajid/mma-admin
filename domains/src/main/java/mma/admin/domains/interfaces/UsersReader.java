package mma.admin.domains.interfaces;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.repository.UsersRepository;

import java.util.List;
import java.util.Set;

public interface UsersReader {

    void readAllUsers();
    List<User> getUsers();
}
