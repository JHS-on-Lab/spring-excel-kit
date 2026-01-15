package me.son.excelkit.sample.member.domain.service.impl;

import lombok.RequiredArgsConstructor;
import me.son.excelkit.sample.member.domain.model.Member;
import me.son.excelkit.sample.member.domain.repository.MemberRepository;
import me.son.excelkit.sample.member.domain.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public void saveAll(List<Member> members) {
        memberRepository.saveAll(members);
    }

    @Override
    public int count() {
        return memberRepository.count();
    }

    @Override
    public void createDummyMembers() {

        List<Member> members = IntStream.range(0, 10)
                .mapToObj(i -> new Member(
                        null,
                        "user-" + UUID.randomUUID().toString().substring(0, 8),
                        "user" + i + "@example.com",
                        20 + (i % 10)
                ))
                .toList();

        memberRepository.saveAll(members);
    }
}
