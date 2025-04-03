package com.currenjin.domain.repository;

import com.currenjin.domain.Member;
import com.currenjin.domain.Member.MemberStatus;
import com.currenjin.domain.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findByNameAndAgeGreaterThan(String name, int age) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(
                        member.name.eq(name),
                        member.age.gt(age)
                )
                .fetch();
    }

    @Override
    public List<Member> findByStatus(MemberStatus status) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(member.status.eq(status))
                .fetch();
    }

    @Override
    public List<Member> findByCity(String city) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(member.address.city.eq(city))
                .fetch();
    }

    @Override
    public List<Member> searchMembers(String nameContains, Integer ageGoe, Integer ageLoe, MemberStatus status) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(
                        nameContains(nameContains),
                        ageGoe(ageGoe),
                        ageLoe(ageLoe),
                        statusEq(status)
                )
                .fetch();
    }

    private BooleanExpression nameContains(String nameContains) {
        if (nameContains == null) {
            return null;
        }
        return QMember.member.name.contains(nameContains);
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        if (ageGoe == null) {
            return null;
        }
        return QMember.member.age.goe(ageGoe);
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        if (ageLoe == null) {
            return null;
        }
        return QMember.member.age.loe(ageLoe);
    }

    private BooleanExpression statusEq(MemberStatus status) {
        if (status == null) {
            return null;
        }
        return QMember.member.status.eq(status);
    }
}