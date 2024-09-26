package com.Alchive.backend.domain.algorithmProblem;

import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.problem.Problem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "algorithm_problem")
public class AlgorithmProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ColumnDefault("false")
    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "algorithmId", nullable = false)
    private Algorithm algorithm;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;

}
