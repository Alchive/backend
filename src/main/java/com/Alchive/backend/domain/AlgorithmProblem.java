package com.Alchive.backend.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "algorithm_problem")
public class AlgorithmProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "algo_proId", columnDefinition = "INT")
    private Long algoProId;

    @ManyToOne
    @JoinColumn(name = "algorithmId", nullable = false)
    private Algorithm algorithm;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;

}
