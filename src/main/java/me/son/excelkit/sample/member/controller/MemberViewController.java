package me.son.excelkit.sample.member.controller;

import lombok.RequiredArgsConstructor;
import me.son.excelkit.sample.member.domain.model.Member;
import me.son.excelkit.sample.member.domain.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberViewController {
    private final MemberService memberService;

    @GetMapping
    public String memberListPage(Model model) {

        // 샘플 데이터 없으면 생성
        if (memberService.count() == 0) {
            memberService.createDummyMembers();
        }

        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "member/list";
    }
}
