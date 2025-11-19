package com.semicolon.backend.domain.member.entity;

import com.semicolon.backend.domain.dailyUse.entity.DailyUse;
import com.semicolon.backend.domain.dailyUse.entity.DailyUseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_member_seq")
    @SequenceGenerator(name = "tbl_member_seq", sequenceName = "TBL_MEMBER_SEQ", allocationSize = 1)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", length = 10)
    @Builder.Default
    private MemberRole memberRole = MemberRole.USER;

    @Column(name = "email", nullable = false, length = 100)
    private String memberEmail;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String memberPhoneNumber;

    @Column(name = "member_gender", nullable = false, length = 10)
    private String memberGender;

    @Column(name = "birth_date", nullable = false)
    private LocalDateTime memberBirthDate;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime memberJoinDate;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @Builder.Default
    private List<DailyUse> dailyUses = new ArrayList<>();

}
