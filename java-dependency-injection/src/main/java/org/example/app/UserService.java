package org.example.app;


import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Inject;
import org.example.infrastructure.annotation.Qualifier;

public class UserService {

    @Inject
    private UserRepository userRepository;
}
