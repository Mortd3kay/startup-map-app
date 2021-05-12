package com.skyletto.startappbackend.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "projects_roles")
public class ProjectAndRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @MapsId("project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
    @MapsId("role_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProjectRole role;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column(name = "has_salary")
    private boolean hasSalary;
    @Column(name = "salary_type")
    private char salaryType = '%';
    @Column(name = "salary_amount")
    private double salaryAmount;

    public ProjectAndRole(Project project, ProjectRole role, User user, boolean hasSalary, char salaryType, double salaryAmount) {
        this.project = project;
        this.role = role;
        this.user = user;
        this.hasSalary = hasSalary;
        this.salaryType = salaryType;
        this.salaryAmount = salaryAmount;
    }

    public ProjectAndRole(Project project, ProjectRole role) {
        this.project = project;
        this.role = role;
    }

    public ProjectAndRole() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectRole role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isHasSalary() {
        return hasSalary;
    }

    public void setHasSalary(boolean hasSalary) {
        this.hasSalary = hasSalary;
    }

    public char getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(char salaryType) {
        this.salaryType = salaryType;
    }

    public double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    @Override
    public String toString() {
        return "ProjectAndRole{" +
                "id=" + id +
                ", project=" + project +
                ", role=" + role.getId() +
                ", user=" + user +
                ", hasSalary=" + hasSalary +
                ", salaryType=" + salaryType +
                ", salaryAmount=" + salaryAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectAndRole that = (ProjectAndRole) o;
        return id == that.id && hasSalary == that.hasSalary && salaryType == that.salaryType && Double.compare(that.salaryAmount, salaryAmount) == 0 && Objects.equals(project, that.project) && Objects.equals(role, that.role) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, project, role, user, hasSalary, salaryType, salaryAmount);
    }
}
