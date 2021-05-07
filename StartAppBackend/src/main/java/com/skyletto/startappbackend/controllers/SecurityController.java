package com.skyletto.startappbackend.controllers;

import com.skyletto.startappbackend.entities.Message;
import com.skyletto.startappbackend.entities.Tag;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.entities.requests.ChatRequest;
import com.skyletto.startappbackend.entities.requests.LoginDataRequest;
import com.skyletto.startappbackend.entities.requests.RegisterDataRequest;
import com.skyletto.startappbackend.entities.responses.ProfileResponse;
import com.skyletto.startappbackend.services.MessageService;
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
    private MessageService messageService;

    private JwtProvider jwtProvider;

    @Autowired
    public SecurityController(UserService userService, TagService tagService, MessageService messageService) {
        this.userService = userService;
        this.tagService = tagService;
        this.messageService = messageService;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/tags/random")
    public @ResponseBody List<Tag> getRandomTags() {
        return tagService.getRandomTags();
    }

    @PostMapping("/messages/add")
    public @ResponseBody Message saveMessage(Authentication auth, @RequestBody Message message){
        User u = userService.findUserByEmail(auth.getName());
        if (u != null) {
            return messageService.saveMessage(message);
        }
        return null;
    }

    @GetMapping("/messages/get")
    public @ResponseBody List<Message> getMessages(Authentication auth, @RequestBody ChatRequest chat){
        User u = userService.findUserByEmail(auth.getName());
        if (u != null) {
            return messageService.getMessagesByChat(u, chat);
        }
        return null;
    }

    @GetMapping("/tags")
    public @ResponseBody List<Tag> getTags(@RequestParam String string) {
        return tagService.getSimilarTags(string);
    }

    @GetMapping("/email")
    public @ResponseBody int findUserByEmail(@RequestParam String email) {
        return userService.countUserByEmail(email);
    }

    @GetMapping("/users/specific")
    public @ResponseBody List<User> getAllUsersByIds(@RequestBody Set<Long> ids){
        return userService.getAllUsersByIds(ids);
    }

    @GetMapping("/user/get")
    public @ResponseBody
    User getUserInfo(Authentication auth) {
        return userService.findUserByEmail(auth.getName());
    }
    
    @GetMapping("/messages/chats")
    public @ResponseBody
    List<Message> getLastMessages(Authentication auth){
        User u = userService.findUserByEmail(auth.getName());
        if (u!=null){
            return messageService.getLastMessages(u);
        }
        return null;
    }

    @PostMapping("/register")
    public @ResponseBody
    ProfileResponse registerUser(@RequestBody @Valid RegisterDataRequest data) {
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
