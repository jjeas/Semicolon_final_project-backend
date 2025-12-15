package com.semicolon.backend.domain.admin.service;

import com.semicolon.backend.domain.admin.dto.DashboardCountDTO;
import com.semicolon.backend.domain.admin.dto.DashboardDTO;

import java.util.List;

public interface AdminService {
    public List<DashboardDTO> getList();
    public DashboardCountDTO getCount();
}
