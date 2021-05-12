package com.skyletto.startappbackend.entities;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "project_roles")
public class ProjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectAndRole> roles;

    public ProjectRole(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProjectRole(String name) {
        this(0, name);
    }

    public ProjectRole() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProjectRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectRole that = (ProjectRole) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
