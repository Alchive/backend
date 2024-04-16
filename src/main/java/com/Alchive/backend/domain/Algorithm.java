package com.Alchive.backend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "algorithm")
public class Algorithm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "algorithmId", columnDefinition = "INT")
    private Long algorithmId;

    @Column(name = "algorithmName", nullable = false)
    private String algorithmName;

    @Builder
    public Algorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}
