package com.currenjin.domain.repository;

import com.currenjin.domain.Member;
import com.currenjin.domain.Member.MemberStatus;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findByNameAndAgeGreaterThan(String name, int age);
    List<Member> findByStatus(MemberStatus status);
    List<Member> findByCity(String city);
    List<Member> searchMembers(String nameContains, Integer ageGoe, Integer ageLoe, MemberStatus status);
}