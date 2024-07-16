package org.jungppo.bambooforest.member.domain.repository;

import static org.jungppo.bambooforest.member.domain.entity.QMemberEntity.memberEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jungppo.bambooforest.member.domain.entity.MemberEntity;
import org.jungppo.bambooforest.member.dto.MemberDto;
import org.jungppo.bambooforest.member.dto.QMemberDto;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements QuerydslMemberRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MemberEntity> findByName(final String name) {
        return Optional.ofNullable(
                queryFactory.selectFrom(memberEntity)
                        .where(nameEquals(name))
                        .fetchOne()
        );
    }

    @Override
    public Optional<MemberDto> findDtoById(final Long id) {
        return Optional.ofNullable(
                queryFactory.select(new QMemberDto(
                                memberEntity.id,
                                memberEntity.oAuth2,
                                memberEntity.username,
                                memberEntity.profileImage,
                                memberEntity.role,
                                memberEntity.batteryCount,
                                memberEntity.createdAt
                        ))
                        .from(memberEntity)
                        .where(idEquals(id))
                        .fetchOne()
        );
    }

    private BooleanExpression nameEquals(final String name) {
        return memberEntity.name.eq(name);
    }

    private BooleanExpression idEquals(final Long id) {
        return memberEntity.id.eq(id);
    }
}
