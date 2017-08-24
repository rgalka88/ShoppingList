package com.jshvarts.shoppinglist.common.domain.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class AnyList {
    private String name;

    @Exclude
    private List<AnyListItem> items = new ArrayList<>();

    public AnyList() {
    }

    public AnyList(String name, List<AnyListItem> items) {
        this.name = name;
        this.items = items;
    }

    public List<AnyListItem> getItems() {
        return items;
    }

    public void setItems(List<AnyListItem> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }
}
