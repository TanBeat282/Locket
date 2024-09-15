package com.tandev.locket.model.user;

import java.io.Serializable;
import java.util.List;

public class AccountInfo implements Serializable {
    private String kind;
    private List<User> users;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
