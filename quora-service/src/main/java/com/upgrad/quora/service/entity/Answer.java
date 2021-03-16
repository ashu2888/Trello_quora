package com.upgrad.quora.service.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Repository
@Table(name= "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="uuid")
    @Size(max=200)
    private String uuid;

    @Column(name="ans")
    @Size(max=255)
    private String ans;

    @Column(name="date")
    @Size(max=6)
    private LocalTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User_Entity user;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

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

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
