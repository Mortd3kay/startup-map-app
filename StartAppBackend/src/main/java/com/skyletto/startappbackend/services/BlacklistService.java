package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.*;
import com.skyletto.startappbackend.entities.requests.LatLngRequest;
import com.skyletto.startappbackend.repositories.LocationRepository;
import com.skyletto.startappbackend.repositories.ProjectRepository;
import com.skyletto.startappbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BlacklistService {

    private static final double ROLE_COEFFICIENT = 0.15;
    private static final double TAGS_COUNT_COEFFICIENT = 0.05;
    private static final double TAGS_EQUALS_COEFFICIENT = 1;
    private static final double DISTANCE_COEFFICIENT = -0.25;
    private static final double DESCRIPTION_COEFFICIENT = 0.15;
    private static final double EXPERIENCE_COEFFICIENT = 0.3;

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private LocationRepository locationRepository;

    @Autowired
    public BlacklistService(ProjectRepository projectRepository, UserRepository userRepository, LocationRepository locationRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    public List<Project> getRecommendationsForUser(LatLngRequest llr, User user) {
        Logger.getLogger("BLACKLIST_SERVICE").log(Level.INFO, "Coordinates: " + llr.getLat() + " " + llr.getLng() + " user: " + user.getId());
        List<Project> projects = projectRepository.findAllRecommendedProjects(llr.getLat(), llr.getLng(), user.getId());
        projects.sort((o1, o2) -> -Double.compare(summarizeProject(o2, user, llr), summarizeProject(o1, user, llr)));
        return projects.stream().limit(4).collect(Collectors.toList());
    }

    public void addProjectToBlacklist(Project project, User user){
        Logger.getLogger("BLACKLIST_SERVICE").log(Level.INFO, "To blacklist: project" + project);

    }

    public List<User> getRecommendationsForProject(Project project) {
        Logger.getLogger("BLACKLIST_SERVICE").log(Level.INFO, "Coordinates: " + project.getLat() + " " + project.getLng() + " project: " + project.getId());
        List<User> users = userRepository.findAllRecommendedUsers(project.getLat(), project.getLng(), project.getId());
        users.sort((o1, o2) -> -Double.compare(summarizeUser(o2, project), summarizeUser(o1, project)));
        return users.stream().limit(4).collect(Collectors.toList());
    }

    private double summarizeUser(User u, Project p) {
        double p1 = u.getTags().size() * TAGS_COUNT_COEFFICIENT;
        double p2 = countOfEqualsTags(p, u) * TAGS_EQUALS_COEFFICIENT;
        double p3 = distanceInAbstractPoints(u.getLat(), p.getLat(), u.getLng(), p.getLng()) * DISTANCE_COEFFICIENT;
        String str = (u.getDescription()==null?"":u.getDescription()) + (u.getTitle()==null?"":u.getTitle());
        double p4 = descriptionLengthInAbstractPoints(str) * DESCRIPTION_COEFFICIENT;
        double p5 = countExperience(u.getExperience()) * EXPERIENCE_COEFFICIENT;
        return p1 + p2 + p3 + p4 + p5;
    }

    private double summarizeProject(Project p, User u, LatLngRequest llr) {
        double p1 = countOfEmptyRoles(p) * ROLE_COEFFICIENT;
        double p2 = p.getTags().size() * TAGS_COUNT_COEFFICIENT;
        double p3 = distanceInAbstractPoints(p.getLat(), llr.getLat(), p.getLng(), llr.getLng()) * DISTANCE_COEFFICIENT;
        double p4 = countOfEqualsTags(p, u) * TAGS_EQUALS_COEFFICIENT;
        double p5 = descriptionLengthInAbstractPoints(p.getDescription() + p.getTitle()) * DESCRIPTION_COEFFICIENT;
        return p1 + p2 + p3 + p4 + p5;
    }

    private int countExperience(String exp){
        if (exp!=null && !exp.isEmpty()){
            try {
                return Integer.parseInt(exp);
            } catch (Exception e){
                Logger.getLogger("BLACKLIST_SERVICE").log(Level.INFO,"Error", e);
            }
        }
        return 0;
    }

    private int countOfEmptyRoles(Project p) {
        int count = 0;
        for (ProjectAndRole r : p.getRoles()) {
            if (r.getUser() == null) count++;
        }
        return count;
    }

    private double distanceInAbstractPoints(double lat1, double lng1, double lat2, double lng2) {
        double latDiff = Math.abs(lat1 - lat2);
        double lngDiff = Math.abs(lng1 - lng2);
        double distance = Math.sqrt(latDiff * latDiff + lngDiff * lngDiff);
        return distance / 0.005;
    }

    private int countOfEqualsTags(Project p, User u) {
        int count = 0;
        if (p.getTags()==null || u.getTags()==null) return 0;
        Set<Tag> tags = p.getTags();
        for (Tag t : u.getTags()) {
            if (tags.contains(t)) count++;
        }
        return count;
    }

    private float descriptionLengthInAbstractPoints(String s) {
        return s.length() / 100f;
    }

}
