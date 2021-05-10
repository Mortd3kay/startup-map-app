package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.Role;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.entities.requests.RegisterDataRequest;
import com.skyletto.startappbackend.repositories.RoleRepository;
import com.skyletto.startappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public User createUser(RegisterDataRequest data) {
        Role role = roleRepository.findRoleByName("ROLE_USER");
        User user = new User(
                data.getEmail(),
                data.getPassword(),
                data.getFirstName(),
                data.getSecondName(),
                data.getPhoneNumber(),
                data.getTitle(),
                data.getExperience(),
                data.getDescription(),
                role,
                data.getTags()
        );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Logger.getLogger("SERVICE").log(Level.INFO, "save "+user.getEmail());
        return userRepository.save(user);
    }

    public List<User> getAllUsersByIds(Set<Long> ids){
        return userRepository.findAllById(ids);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User changeUser(User user) {
        Role role = roleRepository.findRoleByName("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole()==null?role: user.getRole());
        return userRepository.save(user);
    }

    public int countUserByEmail(String email){
        User u = userRepository.findUserByEmail(email);
        if (u!=null) return 1;
        return 0;
    }

    public User findUserByEmail(String email){
        Logger.getLogger("USER_SERVICE").log(Level.INFO, "request info "+email);
        return userRepository.findUserByEmail(email);
    }

    public User findByEmailAndPassword(String email, String password){
        User user = userRepository.findUserByEmail(email);
        if (user!=null){
            if (passwordEncoder.matches(password,user.getPassword()))
                return user;
        }
        return null;
    }
}
