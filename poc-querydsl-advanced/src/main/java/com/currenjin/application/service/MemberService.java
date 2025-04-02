package com.currenjin.application.service;

import com.currenjin.domain.Member;
import com.currenjin.domain.Member.MemberStatus;
import com.currenjin.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 중복 회원 검증 로직
        List<Member> findMembers = memberRepository.findByNameAndAgeGreaterThan(member.getName(), 0);
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    public List<Member> findByStatus(MemberStatus status) {
        return memberRepository.findByStatus(status);
    }

    public List<Member> findByCity(String city) {
        return memberRepository.findByCity(city);
    }

    public List<Member> searchMembers(String nameContains, Integer ageGoe, Integer ageLoe, MemberStatus status) {
        return memberRepository.searchMembers(nameContains, ageGoe, ageLoe, status);
    }
}