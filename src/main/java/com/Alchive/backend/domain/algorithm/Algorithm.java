package com.Alchive.backend.domain.algorithm;

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
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Builder
    public Algorithm(Long algorithmId, String algorithmName) {
        this.id = algorithmId;
        this.name = algorithmName;
    }
}
