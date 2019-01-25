package com.mree.transportsample.model;

import java.util.ArrayList;
import java.util.List;

public class Place {
    private Integer id;
    private String name;
    private List<Integer> paths;

    public Place() {
        paths=new ArrayList<Integer>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPaths() {
        return paths;
    }

    public void setPaths(List<Integer> paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return getName();
    }
}
