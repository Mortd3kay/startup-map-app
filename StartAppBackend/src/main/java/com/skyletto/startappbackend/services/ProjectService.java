package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.Project;
import com.skyletto.startappbackend.entities.ProjectAndRole;
import com.skyletto.startappbackend.entities.ProjectRole;
import com.skyletto.startappbackend.entities.User;
import com.skyletto.startappbackend.repositories.ProjectAndRoleRepository;
import com.skyletto.startappbackend.repositories.ProjectRepository;
import com.skyletto.startappbackend.repositories.ProjectRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private ProjectAndRoleRepository projectAndRoleRepository;
    private ProjectRoleRepository projectRoleRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectAndRoleRepository projectAndRoleRepository, ProjectRoleRepository projectRoleRepository) {
        this.projectRepository = projectRepository;
        this.projectAndRoleRepository = projectAndRoleRepository;
        this.projectRoleRepository = projectRoleRepository;
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

    public List<ProjectRole> getAllRoles(){
        return projectRoleRepository.findAll();
    }
}
