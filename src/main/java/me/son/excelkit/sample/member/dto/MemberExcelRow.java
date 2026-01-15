package me.son.excelkit.sample.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberExcelRow {
    private String name;
    private String email;
    private Integer age;
}
