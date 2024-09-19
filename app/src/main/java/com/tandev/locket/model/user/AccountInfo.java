package com.tandev.locket.model.user;

import java.io.Serializable;
import java.util.List;

public class AccountInfo implements Serializable {
    private String kind;
    private List<AccountInfoUser> users;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<AccountInfoUser> getUsers() {
        return users;
    }

    public void setUsers(List<AccountInfoUser> users) {
        this.users = users;
    }
}
