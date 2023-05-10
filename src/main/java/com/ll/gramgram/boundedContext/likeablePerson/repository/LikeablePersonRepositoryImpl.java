package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.likeablePerson;

@RequiredArgsConstructor
public class LikeablePersonRepositoryImpl implements LikeablePersonRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LikeablePerson> findQslByFromInstaMemberIdAndToInstaMember_username(long fromInstaMemberId, String toInstaMemberUsername) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(likeablePerson)
                        .where(
                                likeablePerson.fromInstaMember.id.eq(fromInstaMemberId)
                                        .and(
                                                likeablePerson.toInstaMember.username.eq(toInstaMemberUsername)
                                        )
                        )
                        .fetchOne()
        );
    }
    @Override
    public List<LikeablePerson> findQslByFields(InstaMember member, String gender, String attractiveCode, String sortCode) {
        //인기순으로 구현하는 것은 실패
        BooleanExpression genderExpression = StringUtils.hasLength(gender)?
                likeablePerson.fromInstaMember.gender.eq(gender): null;
        BooleanExpression attractiveExpression = StringUtils.hasLength(attractiveCode)?
                likeablePerson.attractiveTypeCode.eq(Integer.parseInt(attractiveCode)): null;
        OrderSpecifier<?> orderSpecifier=null;
        int _sortCode = sortCode.isEmpty()? 1 : Integer.parseInt(sortCode);
        switch (_sortCode) {
            case 1 -> orderSpecifier = likeablePerson.createDate.asc();
            case 2 -> orderSpecifier = likeablePerson.createDate.desc();
            case 3 -> {
                //잘못된 결과
                return jpaQueryFactory.selectFrom(likeablePerson)
                        .where(likeablePerson.toInstaMember.eq(member),
                                likeablePerson.fromInstaMember.in(
                                JPAExpressions.select(likeablePerson.toInstaMember)
                                        .from(likeablePerson)
                                        .where(genderExpression, attractiveExpression)
                                        .groupBy(likeablePerson.toInstaMember)
                                        .orderBy(likeablePerson.count().desc())
                                        .limit(100)
                        ))
                        .from(likeablePerson)
                        .fetch();
            }
            case 4->{
                //구현 못했습니다
            }
            case 5 -> {
                return jpaQueryFactory.selectFrom(likeablePerson)
                    .where(genderExpression, attractiveExpression, likeablePerson.toInstaMember.eq(member))
                    .orderBy(likeablePerson.fromInstaMember.gender.desc(), likeablePerson.createDate.asc())
                    .fetch();
            }
            case 6 -> {
                return jpaQueryFactory.selectFrom(likeablePerson)
                        .where(genderExpression, attractiveExpression, likeablePerson.toInstaMember.eq(member))
                        .orderBy(likeablePerson.attractiveTypeCode.asc(), likeablePerson.createDate.asc())
                        .fetch();
            }
        }
        return jpaQueryFactory.selectFrom(likeablePerson)
                        .where(genderExpression, attractiveExpression, likeablePerson.toInstaMember.eq(member))
                        .orderBy(orderSpecifier)
                        .fetch();
    }

}
