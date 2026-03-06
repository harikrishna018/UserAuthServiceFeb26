package dev.umang.userauthservice.services;

import dev.umang.userauthservice.models.User;
import dev.umang.userauthservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);

        if(optionalUser.isEmpty()){
            throw new RuntimeException("User with id " + id + " not found");
        }

        return optionalUser.get();
    }
}
