package me.son.excelkit.sample.member.controller;

import lombok.RequiredArgsConstructor;

import me.son.excelkit.core.reader.ExcelReader;
import me.son.excelkit.sample.member.domain.model.Member;
import me.son.excelkit.sample.member.domain.service.MemberService;
import me.son.excelkit.sample.member.dto.MemberExcelRow;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/members/excel")
@RequiredArgsConstructor
public class MemberExcelController {
    private final ExcelReader excelReader;
    private final MemberService memberService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMemberExcel(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Excel file is required");
        }

        try {
            // Excel → DTO
            List<MemberExcelRow> excelRows = excelReader.read(file.getInputStream(), MemberExcelRow.class);

            // DTO → Domain
            List<Member> members = excelRows.stream()
                    .filter(r -> r.getName() != null && !r.getName().isBlank())
                    .map(this::toMember)
                    .toList();

            // DB 저장
            memberService.saveAll(members);

            return ResponseEntity.ok(
                    "Excel uploaded successfully. Rows: " + members.size()
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to process Excel file: " + e.getMessage());
        }
    }

    private Member toMember(MemberExcelRow row) {
        return new Member(
                null,
                row.getName(),
                row.getEmail(),
                row.getAge()
        );
    }
}
