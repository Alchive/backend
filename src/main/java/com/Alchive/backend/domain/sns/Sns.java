package com.Alchive.backend.domain.sns;

import com.Alchive.backend.domain.BaseEntity;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@SQLDelete(sql = "UPDATE sns SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "sns")
public class Sns extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20, nullable = false)
    private SnsCategory category;

    @Column(name = "token", columnDefinition = "TEXT", nullable = false)
    private String token;

    @Column(name = "channel", columnDefinition = "TEXT")
    private String channel;

    @Column(name = "time", length = 30)
    private String time;

    public static Sns of(User user, SnsCreateRequest snsCreateRequest) {
        return Sns.builder()
                .user(user)
                .category(snsCreateRequest.getCategory())
                .token(snsCreateRequest.getToken())
                .channel(snsCreateRequest.getChannel())
                .time(snsCreateRequest.getTime())
                .build();
    }
}
