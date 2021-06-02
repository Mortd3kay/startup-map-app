package com.skyletto.startappbackend.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "user_blacklists")
@IdClass(BlacklistId.class)
public class UserBlacklist {
    @Id
    @Column(name = "user_id")
    private long id;
    @Id
    @Column(name = "project_id")
    private long blocked_id;

    public UserBlacklist() {
    }

    public UserBlacklist(long user_id, long blocked_id) {
        this.id = user_id;
        this.blocked_id = blocked_id;
    }

    public long getUser_id() {
        return id;
    }

    public void setUser_id(long user_id) {
        this.id = user_id;
    }

    public long getBlocked_id() {
        return blocked_id;
    }

    public void setBlocked_id(long blocked_id) {
        this.blocked_id = blocked_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBlacklist blacklist = (UserBlacklist) o;
        return id == blacklist.id && blocked_id == blacklist.blocked_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blocked_id);
    }
}

@Entity(name = "project_blacklists")
@IdClass(BlacklistId.class)
class ProjectBlacklist{
    @Id
    @Column(name = "project_id")
    private long id;
    @Id
    @Column(name = "user_id")
    private long blocked_id;

    public ProjectBlacklist(long project_id, long blocked_id) {
        this.id = project_id;
        this.blocked_id = blocked_id;
    }

    public ProjectBlacklist() {
    }

    public long getProject_id() {
        return id;
    }

    public void setProject_id(long project_id) {
        this.id = project_id;
    }

    public long getBlocked_id() {
        return blocked_id;
    }

    public void setBlocked_id(long blocked_id) {
        this.blocked_id = blocked_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectBlacklist that = (ProjectBlacklist) o;
        return id == that.id && blocked_id == that.blocked_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blocked_id);
    }
}

class BlacklistId implements Serializable {
    private long id;
    private long blocked_id;

    public BlacklistId() {
    }

    public BlacklistId(long id, long blocked_id) {
        this.id = id;
        this.blocked_id = blocked_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBlocked_id() {
        return blocked_id;
    }

    public void setBlocked_id(long blocked_id) {
        this.blocked_id = blocked_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistId that = (BlacklistId) o;
        return id == that.id && blocked_id == that.blocked_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blocked_id);
    }
}

