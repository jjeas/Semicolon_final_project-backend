package com.semicolon.backend.domain.member.dto;

import com.semicolon.backend.domain.member.entity.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MemberDTO {
    private long memberId;

    @Size(min = 7, max = 16, message = "아이디는 7~16글자로 설정해주세요.")
    @NotBlank(message = "아이디는 필수 입력 정보입니다.")
    private String memberLoginId;
    private String memberName;

    @Size(min = 8, max = 16, message = "비밀번호는 8~16글자로 설정해주세요.")
    @NotBlank(message = "비밀번호는 필수 입력 정보입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
            message = "비밀번호는 영문, 숫자, 특수문자 포함 8~16자여야 합니다." )
    private String memberPassword;
    private String socialLogin;

    private MemberRole memberRole;

    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String memberEmail;
    private String memberAddress;
    private String memberDetailAddress;
    private String memberPhoneNumber;
    private String memberGender;

    private String passwordConfirm;

    @PastOrPresent(message = "미래 날짜는 입력 불가합니다.")
    private LocalDate memberBirthDate;
    private LocalDateTime memberJoinDate;
}
