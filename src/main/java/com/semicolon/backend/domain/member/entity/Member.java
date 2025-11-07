package com.semicolon.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_member_role", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "member_role")
    private List<String> memberRole;

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
}
