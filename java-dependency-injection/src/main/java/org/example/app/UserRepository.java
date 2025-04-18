package org.example.app;

import org.example.infrastructure.annotation.Component;

import java.util.List;


public interface UserRepository {

    void save(User user);

    User getUser(String username);

    List<User> getAll();
}
