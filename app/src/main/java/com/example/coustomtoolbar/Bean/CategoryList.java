package com.example.coustomtoolbar.Bean;

import java.util.List;

/**
 * Created by yaojian on 2017/7/21.
 */

public class CategoryList {

    private List<SocialEncyclopedia> list;

    public List<SocialEncyclopedia> getList() {
        return list;
    }

    public void setList(List<SocialEncyclopedia> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CategoryList{" +
                "list=" + list +
                '}';
    }
}
