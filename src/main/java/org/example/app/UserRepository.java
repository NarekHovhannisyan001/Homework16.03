package org.example.app;

import java.util.List;

public interface UserRepository {

    void save(User user);

    User getUser(String username);

    List<User> getAll();
}
