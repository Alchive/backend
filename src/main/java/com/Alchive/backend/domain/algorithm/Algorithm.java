package com.Alchive.backend.domain.algorithm;

import com.Alchive.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE algorithm SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "algorithm")
public class Algorithm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    public static Algorithm of(String name) {
        return Algorithm.builder()
                .name(name)
                .build();
    }

}
