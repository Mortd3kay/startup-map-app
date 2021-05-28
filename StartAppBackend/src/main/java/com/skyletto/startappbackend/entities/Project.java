package com.skyletto.startappbackend.entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "projects")
@Table(indexes = @Index(columnList = "lat, lng"))
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
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private double lat;
    private double lng;
    private String address;

    public Project() {
    }

    public Project(long id, String title, String description, Set<Tag> tags, Set<ProjectAndRole> roles, User user, double lat, double lng, String address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.roles = roles;
        this.user = user;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", roles=" + roles +
                ", user=" + user +
                ", geo=" + lat +" "+ lng +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id && Double.compare(project.lat, lat) == 0 && Double.compare(project.lng, lng) == 0 && Objects.equals(title, project.title) && Objects.equals(description, project.description) && Objects.equals(tags, project.tags) && Objects.equals(roles, project.roles) && Objects.equals(user, project.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, user);
    }
}
