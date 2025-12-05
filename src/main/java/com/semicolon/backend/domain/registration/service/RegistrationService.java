package com.semicolon.backend.domain.registration.service;

import com.semicolon.backend.domain.registration.dto.RegistrationDTO;
import java.util.List;

public interface RegistrationService {
     void register(Long registrationId, String loginId) throws Exception;
     void cancel(String loginId, Long registrationId);
     List<RegistrationDTO> getList(String loginId);
    boolean checkRegistrationStatus(String loginId, Long lessonId);
}
