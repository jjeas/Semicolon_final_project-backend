package com.semicolon.backend.domain.registration.service;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.registration.dto.RegistrationDTO;
import com.semicolon.backend.domain.registration.entity.Registration;

import java.util.List;

public interface RegistrationService {
    public void register(Long registrationId, String loginId);
    public void cancel(String loginId, Long registrationId);
    public List<RegistrationDTO> getList(String loginId);
}
