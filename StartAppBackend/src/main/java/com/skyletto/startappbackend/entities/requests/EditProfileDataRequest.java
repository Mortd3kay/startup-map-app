package com.skyletto.startappbackend.entities.requests;

import com.skyletto.startappbackend.entities.Tag;

import java.util.Objects;
import java.util.Set;

public class EditProfileDataRequest{
    private long id;
    private String password;
    private String oldPassword;
    private String email;
    private String firstName;
    private String secondName;
    private String phoneNumber;
    private String title;
    private String experience;
    private String description;
    private Set<Tag> tags;

    public EditProfileDataRequest() {
    }

    public EditProfileDataRequest(long id, String password, String oldPassword, String email, String firstName, String secondName, String phoneNumber, String title, String experience, String description, Set<Tag> tags) {
        this.id = id;
        this.password = password;
        this.oldPassword = oldPassword;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.experience = experience;
        this.description = description;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EditProfileDataRequest{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", title='" + title + '\'' +
                ", experience='" + experience + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditProfileDataRequest that = (EditProfileDataRequest) o;
        return id == that.id && Objects.equals(password, that.password) && Objects.equals(oldPassword, that.oldPassword) && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(secondName, that.secondName) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(title, that.title) && Objects.equals(experience, that.experience) && Objects.equals(description, that.description) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, oldPassword, email, firstName, secondName, phoneNumber, title, experience, description, tags);
    }
}
