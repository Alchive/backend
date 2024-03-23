package com.Alchive.backend.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "algorithm")
public class Algorithm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "algorithmId", columnDefinition = "INT")
    private Long algorithmId;

    @Column(name = "algorithmName", nullable = false)
    private String algorithmName;

}
