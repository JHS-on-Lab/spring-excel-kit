package me.son.excelkit.sample.member.domain.service;

import me.son.excelkit.sample.member.domain.model.Member;

import java.util.List;

public interface MemberService {
    List<Member> findAll();
    void saveAll(List<Member> members);
    int count();
    void createDummyMembers();
}
