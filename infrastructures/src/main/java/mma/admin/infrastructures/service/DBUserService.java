package mma.admin.infrastructures.service;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.DataStorage;
import mma.admin.domains.interfaces.service.UserService;

import java.util.List;

public class DBUserService implements UserService {
    private final DataStorage dataStorage;

    public DBUserService(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    @Override
    public List<User> getAllUsers() {
        return dataStorage.getUserDao().findAll();
    }
}
