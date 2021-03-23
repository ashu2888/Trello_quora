package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name="question")
@NamedQueries({
        @NamedQuery(name = "question.fetchByUuid", query = "select q from QuestionEntity q where q.uuid = :uuid"),
        @NamedQuery(name = "question.fetchByUser", query = "select q from QuestionEntity q where q.user = :user"),
        @NamedQuery(name = "question.fetchAll", query = "select q from QuestionEntity q")
})
public class QuestionEntity  implements Serializable{

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
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}