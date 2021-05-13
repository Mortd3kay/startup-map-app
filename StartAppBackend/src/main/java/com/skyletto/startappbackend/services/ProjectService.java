package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.Project;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project addProject(User user, Project project){
        project.setUser(user);
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByUser(User user){
        return projectRepository.getAllByUser(user);
    }

    public Long removeProject(Project project){
        return projectRepository.deleteProjectById(project.getId());
    }
}
