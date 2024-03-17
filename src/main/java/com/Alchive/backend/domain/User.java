package com.Alchive.backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "userEmail", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "userNickName")
    private String userNickName;

    @Column(name = "userDescription")
    private String userDescription;

    @Column(name = "autoSave", nullable = false)
    private boolean autoSave = true;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

}