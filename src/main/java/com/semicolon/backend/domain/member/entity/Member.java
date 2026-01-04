package com.semicolon.backend.domain.member.entity;

import com.semicolon.backend.domain.dailyUse.entity.DailyUse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long memberId;

    @Column(name = "login_id", nullable = false, length = 100)
    private String memberLoginId;

    @Column(name = "password", nullable = false, length = 100)
    private String memberPassword;

    @Column(name = "member_name", nullable = false, length = 50)
    private String memberName;

    @Column(name = "member_address", nullable = false, length = 500)
    private String memberAddress;

    @Column(name = "member_detail_address", nullable = false, length = 500)
    private String memberDetailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", length = 20)
    @Builder.Default
    private MemberRole memberRole = MemberRole.ROLE_USER;

    @Column(name = "email", nullable = false, length = 100)
    private String memberEmail;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String memberPhoneNumber;

    @Column(name = "member_gender", nullable = false, length = 10)
    private String memberGender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate memberBirthDate;

    @Column(name = "social_login", nullable = false, length = 20)
    private String socialLogin;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime memberJoinDate;

}
