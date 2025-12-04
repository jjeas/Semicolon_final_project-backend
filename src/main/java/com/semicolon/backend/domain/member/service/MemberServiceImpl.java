package com.semicolon.backend.domain.member.service;

import com.semicolon.backend.domain.auth.dto.FindIdRequestDTO;
import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.dto.PasswordChangeDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder; // 1. (추가)
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 2. (추가)

import java.util.*;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository repository;
    @Autowired
    private ModelMapper mapper;

    // 3. (추가) 비밀번호 암호화를 위해 PasswordEncoder 주입
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 4. (★핵심 수정★) "수정"을 위한 modify 메서드
    @Override
    @Transactional // 5. (추가) 이 메서드가 끝나면 JPA가 "자동"으로 변경을 감지하고 UPDATE
    public void modify(String loginIdFromToken, MemberDTO requestDTO) {

        // 6. (수정) "loginId"로 "진짜" Member 엔티티를 DB에서 조회합니다.
        Member member = repository.findByMemberLoginId(loginIdFromToken)
                .orElseThrow(() -> new NoSuchElementException("해당 로그인 ID의 회원이 없습니다."));

        // 7. (수정) Builder가 아니라 "Setter"로 엔티티의 값을 "직접" 변경합니다.
        //    (Member 엔티티에 @Setter가 있거나, setXxx() 메서드들이 있어야 합니다.)
        //    (null이 아닐 때만 변경하도록 방어 로직 추가)
        if (requestDTO.getMemberEmail() != null) {
            member.setMemberEmail(requestDTO.getMemberEmail());
        }
        if (requestDTO.getMemberAddress() != null) {
            member.setMemberAddress(requestDTO.getMemberAddress());
        }
        if (requestDTO.getMemberPhoneNumber() != null) {
            member.setMemberPhoneNumber(requestDTO.getMemberPhoneNumber());
        }
        if (requestDTO.getMemberName() != null) {
            member.setMemberName(requestDTO.getMemberName());
        }

        // 8. (주의!) 비밀번호는 "항상" 암호화해서 저장해야 합니다.
        if (requestDTO.getMemberPassword() != null && !requestDTO.getMemberPassword().isEmpty()) {
            member.setMemberPassword(passwordEncoder.encode(requestDTO.getMemberPassword()));
        }

        // 9. (주의!) ID, LoginId, Role, JoinDate는 "수정"하면 안 되므로
        //    여기 Setter 로직에 없습니다. (보안)

        // 10. @Transactional이 붙어있으므로,
        //     메서드가 끝나면 "변경된" member 객체를 JPA가 알아서 DB에 UPDATE 쿼리를 날려줍니다.

    }


    @Override
    public PartnerDTO getPartnerStatus(String loginIdFromToken) {
        Long memberId = repository.findByMemberLoginId(loginIdFromToken).orElseThrow().getMemberId();
        log.info("long memberid==+==> {}", memberId);
        return repository.getPartnerStatus(memberId)
                .orElse(new PartnerDTO((PartnerStatus) null));
    }

    @Override
    public MemberDTO getOneByLoginId(String loginId) {
        // UserDetailService에서 사용했던 repository 메서드를 그대로 활용합니다.
        Member member = repository.findByMemberLoginId(loginId)
                .orElseThrow(() -> new NoSuchElementException("해당 로그인 ID에 해당되는 회원이 없습니다."));
        return mapper.map(member, MemberDTO.class);
    }


    @Override
    public void changePassword(String loginIdFromToken, PasswordChangeDTO passwordChangeDTO) {
        Long memberId = repository.findByMemberLoginId(loginIdFromToken).orElseThrow().getMemberId();
        Member member = repository.findById(memberId).orElseThrow(() -> new NoSuchElementException("해당 ID에 해당되는 회원이 없습니다."));

        if (!passwordChangeDTO.getMemberCurrentPassword().equals(member.getMemberPassword())) {
            throw new NoSuchElementException("기존 비밀번호가 일치하지 않습니다");
        }

        if (!passwordChangeDTO.getMemberPassword().equals(passwordChangeDTO.getMemberPasswordCheck())) {
            throw new IllegalArgumentException("새 비밀번호 확인이 일치하지 않습니다");
        }

        member.setMemberPassword(passwordChangeDTO.getMemberPassword());
        repository.save(member);
    }


    @Override
    public void modifyByAdmin(MemberDTO requestDTO) {
        Member member = repository.findById(requestDTO.getMemberId()).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
        member.setMemberEmail(requestDTO.getMemberEmail());
        member.setMemberPhoneNumber(requestDTO.getMemberPhoneNumber());
        member.setMemberAddress(requestDTO.getMemberAddress());
        member.setMemberGender(requestDTO.getMemberGender());
        member.setMemberBirthDate(requestDTO.getMemberBirthDate());
        member.setMemberRole(requestDTO.getMemberRole());

        repository.save(member);
    }

    @Override
    public PageResponseDTO<MemberDTO> searchMembers(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();
        String role = pageRequestDTO.getRole();

        boolean isKeywordEmpty = (keyword == null || keyword.isBlank());
        boolean isRoleEmpty = (role == null || role.isBlank());

        MemberRole memberRoleEnum = null;
        if (!isRoleEmpty) {
            try {
                memberRoleEnum = MemberRole.valueOf(role);
            } catch (IllegalArgumentException e) {
                log.error("유효하지 않은 MemberRole 값: {}", role);
                return new PageResponseDTO<>(Collections.emptyList(), pageRequestDTO, 0);
            }
        }

        Page<Member> resultPage;

        if (!isRoleEmpty) {
            if (isKeywordEmpty) {
                resultPage = repository.findByMemberRole(memberRoleEnum, pageable);
            } else {
                switch (type) {
                    case "id":
                        resultPage = repository.findByMemberLoginIdContainsAndMemberRole(keyword, memberRoleEnum, pageable);
                        break;
                    case "name":
                        resultPage = repository.findByMemberNameContainsAndMemberRole(keyword, memberRoleEnum, pageable);
                        break;
                    default:
                        resultPage = Page.empty(pageable);
                }
            }
        } else {
            if (isKeywordEmpty) {
                resultPage = repository.findAll(pageable);
            } else {
                switch (type) {
                    case "id":
                        resultPage = repository.findByMemberLoginIdContains(keyword, pageable);
                        break;
                    case "name":
                        resultPage = repository.findByMemberNameContains(keyword, pageable);
                        break;
                    default:
                        resultPage = Page.empty(pageable);
                }
            }
        }

        Page<MemberDTO> dtoPage = resultPage.map(m -> mapper.map(m, MemberDTO.class));

        return new PageResponseDTO<>(dtoPage.getContent(), pageRequestDTO, dtoPage.getTotalElements());
    }

}

