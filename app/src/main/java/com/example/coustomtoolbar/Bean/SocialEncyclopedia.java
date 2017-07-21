package com.example.coustomtoolbar.Bean;

/**
 * Created by yaojian on 2017/7/21.
 */

public class SocialEncyclopedia {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SocialEncyclopedia{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
