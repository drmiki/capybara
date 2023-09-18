package ru.drmiki.help_for_capy.services;

import org.springframework.stereotype.Service;
import ru.drmiki.help_for_capy.classes.User;
import ru.drmiki.help_for_capy.repo.UserRepository;

@Service
public class UserService {


    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User getUserByEmail(String email){
        User user = userRepository.findUserByEmail(email);
        return user;
    }
    public User createUser(User user){


        User newUser = userRepository.save(user);
        userRepository.flush();
        return newUser;
    }
}
