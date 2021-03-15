package com.upgrad.quora.service.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Entity
@Table(name= "user_auth")
public class userAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="uuid")
    @Size(max=200)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User_Entity user;

    @Column(name="access_token")
    @Size(max=500)
    private String accessToken;

    @Column(name="expires_at")
    @Size(max=6)
    private LocalTime expiresAt;

    @Column(name="login_at")
    @Size(max=6)
    private LocalTime loginAt;

    @Column(name="logout_at")
    @Size(max=6)
    private LocalTime logoutAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User_Entity getUser() {
        return user;
    }

    public void setUser(User_Entity user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(LocalTime loginAt) {
        this.loginAt = loginAt;
    }

    public LocalTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(LocalTime logoutAt) {
        this.logoutAt = logoutAt;
    }
}
