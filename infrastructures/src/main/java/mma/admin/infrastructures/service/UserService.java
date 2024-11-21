package mma.admin.infrastructures.service;

import mma.admin.domains.User;
import mma.admin.infrastructures.sql.SQLDataStorage;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final SQLDataStorage dataStorage;

    public UserService(SQLDataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    public void getUser(String id) {
        try{
            dataStorage.getUserDao().findById(id);
        }catch (Exception e) {
            dataStorage.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users=new ArrayList<User>();
        try{
            users=dataStorage.getUserDao().findAll();
            return users;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
