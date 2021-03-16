package com.upgrad.quora.service.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Repository
@Table(name="question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="uuid")
    @Size(max=200)
    private String uuid;

    @Column(name="content")
    @Size(max=200)
    private String content;

    @Column(name="date")
    @Size(max=6)
    private LocalTime date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User_Entity user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalTime getDate() {
        return date;
    }

    public void setDate(LocalTime date) {
        this.date = date;
    }

    public User_Entity getUser() {
        return user;
    }

    public void setUser(User_Entity user) {
        this.user = user;
    }
}
