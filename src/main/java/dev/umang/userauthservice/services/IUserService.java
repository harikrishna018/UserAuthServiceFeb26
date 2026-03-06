package dev.umang.userauthservice.services;

import dev.umang.userauthservice.models.User;

public interface IUserService {
    public User getUserById(Long id);
}
