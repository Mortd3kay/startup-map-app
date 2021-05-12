package com.skyletto.startappbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "projects_tags",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectAndRole> roles;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User creator;

    public Project() {
    }

    public Project(String title, String description, Set<Tag> tags, Set<ProjectAndRole> roles) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<ProjectAndRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ProjectAndRole> roles) {
        this.roles = roles;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", roles=" + roles +
                ", creator=" + creator.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id && Objects.equals(title, project.title) && Objects.equals(description, project.description) && Objects.equals(tags, project.tags) && Objects.equals(roles, project.roles) && Objects.equals(creator, project.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, tags, roles, creator);
    }
}
