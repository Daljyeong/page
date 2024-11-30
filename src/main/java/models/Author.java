package models;

import java.io.Serializable;

public class Author implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int id; // 고유번호

    public Author(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + " #" + id;
    }
}
