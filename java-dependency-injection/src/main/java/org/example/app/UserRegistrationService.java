package org.example.app;


import lombok.Getter;
import org.example.infrastructure.annotation.*;
import org.example.infrastructure.configreader.ObjectConfigReader;

import javax.annotation.PreDestroy;

@Log
@Component
public class UserRegistrationService {

    @Inject
    @Qualifier(UserInMemoryRepository.class)
    private UserRepository userRepository;

    @Inject
    @Qualifier(DefaultEmailSender.class)
    private EmailSender emailSender;

    @Getter
    @Env
    private String env;

    @Getter
    @Env("Chlpppppp")
    private String chlp;

    public void register(User user) {
        User existingUser = userRepository.getUser(user.getUsername());
        if (existingUser != null) {
            throw new UserAlreadyExistsException(
                    "User is already registered. Username: " + user.getUsername()
            );
        }

        userRepository.save(user);

        emailSender.send(
                user.getEmail(),
                "Account confirmation",
                "Please confirm your newly created account"
        );
    }

    @PostConstruct
    public void print() {
        System.out.println("hello world");
    }


    public void foo() {
        System.out.println("hello foo");
    }
    @PostConstruct
    public void init() {
        System.out.println(env + " :: " + chlp);
    }
}
