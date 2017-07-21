package com.example.coustomtoolbar.Bean;

import java.util.List;

/**
 * Created by yaojian on 2017/7/21.
 */

public class AllCategory {
    private String name;
    private List<CategoryList> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryList> getList() {
        return list;
    }

    public void setList(List<CategoryList> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "AllCategory{" +
                "name='" + name + '\'' +
                ", list=" + list +
                '}';
    }
}
