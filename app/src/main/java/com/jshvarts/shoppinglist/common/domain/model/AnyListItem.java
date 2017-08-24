package com.jshvarts.shoppinglist.common.domain.model;

public class AnyListItem {
    private String name;
    private boolean done;

    public AnyListItem() {
    }

    public AnyListItem(String name, boolean done) {
        this.name = name;
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public boolean getDone() {
        return done;
    }
}
