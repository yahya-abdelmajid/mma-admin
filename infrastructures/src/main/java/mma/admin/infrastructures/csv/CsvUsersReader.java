package mma.admin.infrastructures.csv;

import mma.admin.domains.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CsvUsersReader {

    private String csvFile;
    private Set<User> users;
    public CsvUsersReader(String csvFile) {
        this.csvFile = csvFile;
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }


    public Set<User> getUsers() {
        return users;
    }


    public boolean exists(String matricule){
       return users.stream().anyMatch(user -> user.getBadge().equals(matricule));
    }

    public User getUser(String id){
        return users.stream().filter(user -> user.getBadge().equals(id)).findFirst().get();
    }

    //généré par une IA
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
