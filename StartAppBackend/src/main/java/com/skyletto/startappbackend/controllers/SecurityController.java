package com.skyletto.startappbackend.controllers;

import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.entities.requests.LoginDataRequest;
import com.skyletto.startappbackend.entities.requests.RegisterDataRequest;
import com.skyletto.startappbackend.entities.responses.AuthResponse;
import com.skyletto.startappbackend.entities.responses.ProfileResponse;
import com.skyletto.startappbackend.services.UserService;
import com.skyletto.startappbackend.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="api")
public class SecurityController {

    private UserService userService;

    private JwtProvider jwtProvider;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/email")
    public int findUserByEmail(@RequestParam @Valid String email){
        return userService.countUserByEmail(email);
    }

    //тест
    @GetMapping("/user/get")
    @PreAuthorize("#logData.email == authentication.name")
    public @ResponseBody User getUserInfo(@RequestBody LoginDataRequest logData){
        return userService.findUserByEmail(logData.getEmail());
    }

    @PostMapping("/register")
    public @ResponseBody ProfileResponse registerUser(@RequestBody @Valid RegisterDataRequest data){
        User u = userService.createUser(data);
        if (u!=null) {
            String token = jwtProvider.generateToken(data.getEmail());
            ProfileResponse pr = new ProfileResponse(token, u);
            return pr;
        }
        return null;
    }

    @PostMapping("/auth")
    public @ResponseBody ProfileResponse loginUser(@RequestBody @Valid LoginDataRequest data){
        User user = userService.findByEmailAndPassword(data.getEmail(), data.getPassword());
        if (user!=null) {
            String token = jwtProvider.generateToken(user.getEmail());
            return new ProfileResponse(token, user);
        }
        return null;
    }

    @PutMapping("/user/edit")
    public @ResponseBody
    ProfileResponse updateUser(@RequestBody @Valid User user, Authentication authentication){
        User u = userService.findUserByEmail(authentication.getName());
        if (u!=null) {
            user.setId(u.getId());
            u = userService.changeUser(user);
            return new ProfileResponse(jwtProvider.generateToken(u.getEmail()), u);
        }
        return null;
    }
}
