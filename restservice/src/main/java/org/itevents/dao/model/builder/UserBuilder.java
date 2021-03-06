package org.itevents.dao.model.builder;

import org.itevents.dao.model.Role;
import org.itevents.dao.model.User;

/**
 * Created by vaa25 on 29.10.2015.
 */
public class UserBuilder {
    private int id;
    private String login;
    private Role role;
    private boolean subscribed = false;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder id(int id) {
        this.id = id;
        return this;
    }

    public UserBuilder login(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder subscribed(boolean subscribed) {
        this.subscribed = subscribed;
        return this;
    }

    public UserBuilder but() {
        return anUser().id(id).login(login).role(role).subscribed(subscribed);
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setRole(role);
        user.setSubscribed(subscribed);
        return user;
    }
}
