package com.Alchive.backend.domain.algorithmProblem;

import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.problem.Problem;
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
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "algorithmId", nullable = false)
    private Algorithm algorithm;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;

    @Builder
    public AlgorithmProblem(Long algoProId, Algorithm algorithm, Problem problem) {
        this.id = algoProId;
        this.algorithm = algorithm;
        this.problem = problem;
    }

}
