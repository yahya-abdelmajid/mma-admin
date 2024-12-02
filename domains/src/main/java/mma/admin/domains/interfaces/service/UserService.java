package mma.admin.domains.interfaces.service;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.DataStorage;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
}
