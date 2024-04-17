package com.Alchive.backend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
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

    @Builder
    public AlgorithmProblem(Long algoProId, Algorithm algorithm, Problem problem) {
        this.algoProId = algoProId;
        this.algorithm = algorithm;
        this.problem = problem;
    }

}
