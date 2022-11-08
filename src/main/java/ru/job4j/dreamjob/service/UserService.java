package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.UserDBStore;

@Service
public class UserService {

    private final UserDBStore store;

    public UserService(UserDBStore store) {
        this.store = store;
    }

    public User addUSer(User user) {
        return store.addUser(user);
    }
}
