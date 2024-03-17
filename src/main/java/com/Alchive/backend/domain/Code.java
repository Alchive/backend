package com.Alchive.backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "code")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codeId")
    private int codeId;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;

    @Column(name = "codeCorrect", nullable = false)
    private boolean codeCorrect;

    @Column(name = "codeContent", nullable = false)
    private String codeContent;

    @Column(name = "codeMemory", nullable = false)
    private int codeMemory;

    @Column(name = "codeTime", nullable = false)
    private int codeTime;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

}