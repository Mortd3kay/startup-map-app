package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.Role;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.repositories.RoleRepository;
import com.skyletto.startappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        Role role = roleRepository.findRoleByName("ROLE_USER");
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User changeUser(User user) {
        return userRepository.save(user);
    }

    public int countUserByEmail(String email){
        User u = userRepository.findUserByEmail(email);
        if (u!=null) return 1;
        return 0;
    }

    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public User findByEmailAndPassword(String email, String password){
        User user = userRepository.findUserByEmail(email);
        if (user!=null){
            if (passwordEncoder.matches(user.getPassword(),password))
                return user;
        }
        return null;
    }
}
