package mma.admin.domains;

public class Prestation {
    String id;
    String description;

    public Prestation(String id, String description) {
        this.id = id;
        this.description = description;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


}
