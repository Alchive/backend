package com.Alchive.backend.domain.algorithmProblem;

import com.Alchive.backend.domain.BaseEntity;
import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.problem.Problem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE algorithm_problem SET is_deleted = true WHERE id = ?")
@Table(name = "algorithm_problem")
public class AlgorithmProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "algorithm_id", nullable = false)
    private Algorithm algorithm;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    public static AlgorithmProblem of(Algorithm algorithm, Problem problem) {
        return AlgorithmProblem.builder()
                .algorithm(algorithm)
                .problem(problem)
                .build();
    }
}
