package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.Project;
import com.skyletto.startappbackend.entities.ProjectAndRole;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.repositories.ProjectAndRoleRepository;
import com.skyletto.startappbackend.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private ProjectAndRoleRepository projectAndRoleRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectAndRoleRepository projectAndRoleRepository) {
        this.projectRepository = projectRepository;
        this.projectAndRoleRepository = projectAndRoleRepository;
    }

    public Project addProject(User user, Project project){
        project.setUser(user);
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByUser(User user){
        return projectRepository.getAllByUser(user);
    }

    public List<Project> removeProject(User user,Project project){
        projectAndRoleRepository.deleteAllByProject(project);
        projectRepository.delete(project);
        return getProjectsByUser(user);
    }
}
