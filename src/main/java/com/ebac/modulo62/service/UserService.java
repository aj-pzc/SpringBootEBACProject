package com.ebac.modulo62.service;

import com.ebac.modulo62.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User newUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId){
     return userRepository.findById(userId);
    }

    public List<User> getUserList(){
        return userRepository.findAll();
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }
}
