package mma.admin.infrastructures.service;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.DataStorage;

import java.util.List;

public class UserService {
    private final DataStorage dataStorage;

    public UserService(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    public User getUser(String id) {
        return dataStorage.getUserDao().findById(id);
    }

    public List<User> getAllUsers() {
        return dataStorage.getUserDao().findAll();
    }
}
