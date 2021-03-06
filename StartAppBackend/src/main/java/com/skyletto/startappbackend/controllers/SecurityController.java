package com.skyletto.startappbackend.controllers;

import com.skyletto.startappbackend.entities.*;
import com.skyletto.startappbackend.entities.requests.EditProfileDataRequest;
import com.skyletto.startappbackend.entities.requests.LatLngRequest;
import com.skyletto.startappbackend.entities.requests.LoginDataRequest;
import com.skyletto.startappbackend.entities.requests.RegisterDataRequest;
import com.skyletto.startappbackend.entities.responses.ProfileResponse;
import com.skyletto.startappbackend.services.*;
import com.skyletto.startappbackend.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "api")
public class SecurityController {

    private UserService userService;
    private TagService tagService;
    private MessageService messageService;
    private ProjectService projectService;
    private BlacklistService blacklistService;

    private JwtProvider jwtProvider;

    @Autowired
    public SecurityController(UserService userService, TagService tagService, MessageService messageService, ProjectService projectService, BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
        this.userService = userService;
        this.tagService = tagService;
        this.messageService = messageService;
        this.projectService = projectService;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/tags/random")
    public @ResponseBody
    List<Tag> getRandomTags() {
        return tagService.getRandomTags();
    }

    @PostMapping("/messages/add")
    public @ResponseBody
    Message saveMessage(Authentication auth, @RequestBody Message message) {
        if (auth != null && auth.isAuthenticated()) {
            return messageService.saveMessage(message);
        }
        return null;
    }

    @GetMapping("/messages/get")
    public @ResponseBody
    List<Message> getMessages(Authentication auth, @RequestParam Long chat, @RequestParam(name = "last") Long lastId) {
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                return messageService.getMessagesByChat(u, chat, lastId);
            }
        }
        return null;
    }

    @GetMapping("/tags")
    public @ResponseBody
    List<Tag> getTags(@RequestParam String string) {
        return tagService.getSimilarTags(string);
    }

    @GetMapping("/email")
    public @ResponseBody
    int findUserByEmail(@RequestParam String email) {
        return userService.countUserByEmail(email);
    }

    @PostMapping("/users/specific")
    public @ResponseBody
    List<User> getAllUsersByIds(Authentication auth, @RequestBody Set<Long> ids) {
        if (auth != null && auth.isAuthenticated())
            return userService.getAllUsersByIds(ids);
        return null;
    }

    @GetMapping("/users/{id}")
    public @ResponseBody
    User getUserById(Authentication auth, @PathVariable("id") Long id) {
        Logger.getLogger("CONTROLLER").log(Level.INFO, "user_id: " + id);
        if (auth != null && auth.isAuthenticated())
            return userService.getUserById(id);
        return null;
    }

    @GetMapping("/user/get")
    public @ResponseBody
    User getUserInfo(Authentication auth) {
        if (auth != null) {
            return userService.findUserByEmail(auth.getName());
        }
        return null;
    }

    @GetMapping("/messages/chats")
    public @ResponseBody
    List<Message> getLastMessages(Authentication auth) {
        User u = userService.findUserByEmail(auth.getName());
        if (u != null) {
            return messageService.getLastMessages(u);
        }
        return null;
    }

    @PostMapping("/register")
    public @ResponseBody
    ProfileResponse registerUser(@RequestBody @Valid RegisterDataRequest data) {
        int count = tagService.saveTags(data.getTags());
        System.out.println("saved " + count);
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
    ProfileResponse updateUser(@RequestBody @Valid EditProfileDataRequest user, Authentication authentication) {
        if (authentication != null) {
            User u = userService.findUserByEmail(authentication.getName());
            if (u != null) {
                u = userService.changeUser(u, user);
                Logger.getLogger("CONTROLLER").log(Level.INFO, "changed user: " + u);
                if (u == null) return null;
                return new ProfileResponse(jwtProvider.generateToken(u.getEmail()), u);
            }
        }
        return null;
    }

    @PostMapping("/projects/add")
    public @ResponseBody
    Project addProject(Authentication auth, @RequestBody Project project) {
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                try {
                    int count = tagService.saveTags(project.getTags());
                    System.out.println("saved " + count);
                } catch (Exception e) {
                    Logger.getLogger("CONTROLLER").log(Level.INFO, "Projects tags: ", e.getCause());
                }
                for (ProjectAndRole pr :
                        project.getRoles()) {
                    pr.setProject(project);
                }
                return projectService.addProject(u, project);
            }
        }
        return null;
    }

    @GetMapping("/projects/all")
    public @ResponseBody
    List<Project> getAllUserProjects(Authentication auth) {
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                return projectService.getProjectsByUser(u);
            }
        }
        return null;
    }

    @PostMapping("/projects/closest")
    public @ResponseBody
    List<Project> getAllProjectsAround(Authentication auth, @RequestBody LatLngRequest latLng) {
        if (auth != null && auth.isAuthenticated()) {
            return projectService.getLocations(latLng);
        }
        return new ArrayList<>();
    }

    @GetMapping("/roles/all")
    public @ResponseBody
    List<ProjectRole> getAllRoles(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return projectService.getAllRoles();
        }
        return null;
    }

    @DeleteMapping("/projects/remove")
    public @ResponseBody
    List<Project> removeProject(Authentication auth, @RequestBody Project project) {
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                return projectService.removeProject(u, project);
            }
        }
        return null;
    }

    @PostMapping("/projects/recommendations")
    public @ResponseBody
    List<Project> getRecommendationsForUser(Authentication auth, @RequestBody LatLngRequest latLngRequest) {
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                return blacklistService.getRecommendationsForUser(latLngRequest, u);
            }
        }
        return null;
    }

    @PutMapping("/roles/update")
    public @ResponseBody
    ProjectAndRole updateRole(Authentication auth, @RequestBody ProjectAndRole projectAndRole) {
        if (auth != null && auth.isAuthenticated())
            return projectService.updateRole(projectAndRole);
        return null;
    }

    @PostMapping("/user/location")
    public @ResponseBody
    Location setUserLocation(Authentication auth, @RequestBody LatLngRequest latLng) {
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                return userService.setLocation(u.getId(), latLng);
            }
        }
        return null;
    }

    @PostMapping("/users/recommendations")
    public @ResponseBody
    List<User> getRecommendationsForProject(Authentication auth, @RequestBody Project project) {
        if (auth != null && auth.isAuthenticated()) {
            return blacklistService.getRecommendationsForProject(project);
        }
        return null;
    }

    @PostMapping("/users/blacklist/add")
    public void addProjectToBlacklist(Authentication auth, @RequestBody Project project){
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                blacklistService.addProjectToBlacklist(project, u);
            }
        }
    }

    @PostMapping("/projects/blacklist/add")
    public void addUserToBlacklist(Authentication auth, @RequestBody Project project){
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                blacklistService.addProjectToBlacklist(project, u);
            }
        }
    }

    @DeleteMapping("/user/location/remove")
    public void removeLocation(Authentication auth) {
        if (auth != null) {
            User u = userService.findUserByEmail(auth.getName());
            if (u != null) {
                userService.removeLocation(u.getId());
            }
        }
    }

    @PostMapping("/users/locations")
    public @ResponseBody
    List<Location> getLocationsAround(Authentication auth, @RequestBody LatLngRequest latLng) {
        if (auth != null && auth.isAuthenticated()) {
            return userService.getLocations(latLng);
        }
        return new ArrayList<>();
    }
}
