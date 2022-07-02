package pointclub.pointclubclient.model;

import lombok.Data;

@Data
public class User {
    private String name;
    private int id;

    public User(String name) {
        this.name = name;
    }
}
