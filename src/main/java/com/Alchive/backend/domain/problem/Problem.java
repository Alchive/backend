package com.Alchive.backend.domain.problem;

import com.Alchive.backend.domain.BaseEntity;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE problem SET is_deleted = true WHERE id = ?")
@Table(name = "problem")
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "title", nullable = false, length = 60)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "url", nullable = false, length = 300)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false, length = 10)
    private ProblemDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, length = 20)
    private ProblemPlatform platform;

    public static Problem of(ProblemCreateRequest problemCreateRequest) {
        return Problem.builder()
                .number(problemCreateRequest.getNumber())
                .title(problemCreateRequest.getTitle())
                .content(problemCreateRequest.getContent())
                .url(problemCreateRequest.getUrl())
                .difficulty(problemCreateRequest.getDifficulty())
                .platform(problemCreateRequest.getPlatform())
                .build();
    }
}