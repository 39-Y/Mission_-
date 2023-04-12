package com.ll.gramgram.boundedContext.instaMember.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
@Entity
@Getter
public class InstaMember {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    @Column(unique = true)
    private String username;
    @Setter
    private String gender;
    @OneToMany(mappedBy = "toInstaMember", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 1000)
    @JsonIgnore
    private List<LikeablePerson> likedBy;

    @OneToMany(mappedBy = "fromInstaMember", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    @JsonIgnore
    private List<LikeablePerson> likes;
}
