package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long>, LikeablePersonRepositoryCustom {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);

    List<LikeablePerson> findByToInstaMember_username(String username);

    LikeablePerson findByFromInstaMemberIdAndToInstaMember_username(long fromInstaMemberId, String username);

    Optional<LikeablePerson> findByFromInstaMember_usernameAndToInstaMember_username(String fromInstaMemberUsername, String toInstaMemberUsername);
    @Query("SELECT e FROM LikeablePerson e WHERE 1 = 1" +
            " AND (:gender IS NULL OR :gender = '' OR e.fromInstaMember.gender = :gender)" +
            " AND (:attractiveTypeCode IS NULL OR :attractiveTypeCode='' OR e.attractiveTypeCode = :attractiveTypeCode)" +
            " ORDER BY e.createDate ASC")
    List<LikeablePerson> findByFields(@Param("gender") String gender,
                                      @Param("attractiveTypeCode") String attractiveTypeCode
                                      );

}
