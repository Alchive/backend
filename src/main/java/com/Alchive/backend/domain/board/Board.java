package com.Alchive.backend.domain.board;

import com.Alchive.backend.domain.BaseEntity;
import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
@Getter
@Entity
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "board")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "memo")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private BoardStatus status;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public static Board of(Problem problem, User user, BoardCreateRequest boardCreateRequest) {
        return Board.builder()
                .problem(problem)
                .user(user)
                .memo(boardCreateRequest.getMemo())
                .status(boardCreateRequest.getStatus())
                .description(boardCreateRequest.getDescription())
                .build();
    }

    public Board updateMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public Board updateDescription(String description) {
        this.description = description;
        return this;
    }

}