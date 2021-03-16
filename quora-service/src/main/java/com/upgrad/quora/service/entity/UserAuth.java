package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name= "user_auth")

@NamedQueries({
        @NamedQuery(name = "userByAuthToken", query = "select u from UserAuth u where u.accessToken =:token")
})
public class UserAuth {
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
    private ZonedDateTime expiresAt;

    @Column(name="login_at")
    private ZonedDateTime loginAt;

    @Column(name="logout_at")
    @Size(max=6)
    private ZonedDateTime logoutAt;

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

    public @Size(max = 6) ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }
}
