package com.currenjin.presentation;

import com.currenjin.application.service.MemberService;
import com.currenjin.domain.Member;
import com.currenjin.domain.Member.MemberStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public CreateMemberResponse saveMember(@RequestBody CreateMemberRequest request) {
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .status(MemberStatus.ACTIVE)
                .build();

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @GetMapping("/api/members")
    public List<MemberDto> findMembers() {
        List<Member> members = memberService.findMembers();
        return members.stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/members/search")
    public List<MemberDto> searchMembers(
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) Integer ageGoe,
            @RequestParam(required = false) Integer ageLoe,
            @RequestParam(required = false) MemberStatus status) {

        List<Member> members = memberService.searchMembers(nameContains, ageGoe, ageLoe, status);
        return members.stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/members/status/{status}")
    public List<MemberDto> findMembersByStatus(@PathVariable MemberStatus status) {
        List<Member> members = memberService.findByStatus(status);
        return members.stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/members/city/{city}")
    public List<MemberDto> findMembersByCity(@PathVariable String city) {
        List<Member> members = memberService.findByCity(city);
        return members.stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static class CreateMemberRequest {
        private String name;
        private String email;
        private int age;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class MemberDto {
        private Long id;
        private String name;
        private String email;
        private int age;
        private MemberStatus status;
        private String city;

        public MemberDto(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.email = member.getEmail();
            this.age = member.getAge();
            this.status = member.getStatus();
            this.city = member.getAddress() != null ? member.getAddress().getCity() : null;
        }
    }
}