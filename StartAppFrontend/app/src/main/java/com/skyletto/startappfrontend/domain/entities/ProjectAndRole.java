package com.skyletto.startappfrontend.domain.entities;

import java.util.Objects;

public class ProjectAndRole {

    private long id;
    private Project project;
    private ProjectRole role;
    private User user;
    private boolean hasSalary;
    private char salaryType = '%';
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
