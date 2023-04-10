package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);
    @Query("SELECT p FROM LikeablePerson p " +
            "WHERE p.fromInstaMember.id = :fromInstaMemberId AND p.toInstaMember.id = :toInstaMemberId")
    Optional<LikeablePerson> findByFromAndToInstaMember(@Param("fromInstaMemberId") Long fromInstaMemberId,
                                        @Param("toInstaMemberId") Long toInstaMemberId);
    Long countByFromInstaMemberId(Long fromInstaMemberId);
}
