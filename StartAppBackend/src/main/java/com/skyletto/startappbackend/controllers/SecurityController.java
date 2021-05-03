package com.skyletto.startappbackend.controllers;

import com.skyletto.startappbackend.entities.Tag;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.entities.requests.LoginDataRequest;
import com.skyletto.startappbackend.entities.requests.RegisterDataRequest;
import com.skyletto.startappbackend.entities.responses.ProfileResponse;
import com.skyletto.startappbackend.services.TagService;
import com.skyletto.startappbackend.services.UserService;
import com.skyletto.startappbackend.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api")
public class SecurityController {

    private UserService userService;
    private TagService tagService;

    private JwtProvider jwtProvider;

    @Autowired
    public SecurityController(UserService userService, TagService tagService) {
        this.userService = userService;
        this.tagService = tagService;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/tags/random")
    public List<Tag> getRandomTags() {
        return tagService.getRandomTags();
    }

    @GetMapping("tags")
    public List<Tag> getTags(@RequestParam String string) {
        return tagService.getSimilarTags(string);
    }

    @GetMapping("/email")
    public int findUserByEmail(@RequestParam @Valid String email) {
        return userService.countUserByEmail(email);
    }

    @GetMapping("/user/get")
    public @ResponseBody
    User getUserInfo(Authentication auth) {
        return userService.findUserByEmail(auth.getName());
    }

    @PostMapping("/register")
    public @ResponseBody
    ProfileResponse registerUser(@RequestBody @Valid RegisterDataRequest data) {
        System.out.println(data);
        int count = tagService.saveTags(data.getTags());
        System.out.println("saved "+count);
        User u = userService.createUser(data);
        if (u != null) {
            String token = jwtProvider.generateToken(data.getEmail());
            ProfileResponse pr = new ProfileResponse(token, u);
            return pr;
        }
        return null;
    }

    @PostMapping("/auth")
    public @ResponseBody
    ProfileResponse loginUser(@RequestBody @Valid LoginDataRequest data) {
        User user = userService.findByEmailAndPassword(data.getEmail(), data.getPassword());
        if (user != null) {
            String token = jwtProvider.generateToken(user.getEmail());
            return new ProfileResponse(token, user);
        }
        return null;
    }

    @PutMapping("/user/edit")
    public @ResponseBody
    ProfileResponse updateUser(@RequestBody @Valid User user, Authentication authentication) {
        User u = userService.findUserByEmail(authentication.getName());
        if (u != null) {
            user.setId(u.getId());
            u = userService.changeUser(user);
            return new ProfileResponse(jwtProvider.generateToken(u.getEmail()), u);
        }
        return null;
    }
}
