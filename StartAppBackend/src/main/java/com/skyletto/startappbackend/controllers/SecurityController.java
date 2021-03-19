package com.skyletto.startappbackend.controllers;

import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.entities.requests.LoginDataRequest;
import com.skyletto.startappbackend.entities.responses.AuthResponse;
import com.skyletto.startappbackend.services.UserService;
import com.skyletto.startappbackend.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
    public @ResponseBody User getUserInfo(@RequestHeader (name="Authorization") String token){
        String email = jwtProvider.getLoginFromToken(token);
        return userService.findUserByEmail(email);
    }

    @PostMapping("/register")
    public @ResponseBody AuthResponse registerUser(@RequestBody @Valid User user){
        User u = userService.createUser(user);
        if (u!=null) {
            String token = jwtProvider.generateToken(user.getEmail());
            return new AuthResponse(token);
        }
        return null;
    }

    @PostMapping("/auth")
    public @ResponseBody AuthResponse loginUser(@RequestBody @Valid LoginDataRequest loginData){
        User user = userService.findByEmailAndPassword(loginData.getEmail(), loginData.getPassword());
        if (user!=null) {
            String token = jwtProvider.generateToken(user.getEmail());
            return new AuthResponse(token);
        }
        return null;
    }

    @PutMapping("/user/edit")
    public @ResponseBody AuthResponse updateUser(@RequestBody @Valid User user, @RequestParam("Token") String token){
        String email = jwtProvider.getLoginFromToken(token);
        User u = userService.findUserByEmail(email);
        user.setId(u.getId());
        u = userService.changeUser(user);
        return new AuthResponse(jwtProvider.generateToken(u.getEmail()));
    }
}
