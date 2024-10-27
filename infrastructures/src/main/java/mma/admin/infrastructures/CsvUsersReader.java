package mma.admin.infrastructures;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.UsersService;
import mma.admin.domains.interfaces.repository.UsersRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CsvUsersReader implements UsersService {

    private String csvFile;
    private Set<User> users;
    public CsvUsersReader(String csvFile) {
        this.csvFile = csvFile;
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public Set<User> getUsers() {
        return users;
    }

    @Override
    public boolean exists(String matricule){
       return users.stream().anyMatch(user -> user.getMatricule().equals(matricule));
    }

    public User getUser(String id){
        return users.stream().filter(user -> user.getMatricule().equals(id)).findFirst().get();
    }

    //généré par une IA
    @Override
   public void readAllUsers(){
       String line;
       String csvSplitBy = ",";
       Set<User> users = new HashSet<>();
       try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
           // Passe la première ligne
           br.readLine();
           while ((line = br.readLine()) != null) {
               String[] data = line.split(csvSplitBy);
               User user = new User(data[0], data[1], data[2]);
               users.add(user);
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
       this.users= users;
   }

}
