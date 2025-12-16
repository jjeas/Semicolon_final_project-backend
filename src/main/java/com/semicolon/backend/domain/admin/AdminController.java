package com.semicolon.backend.domain.admin;

import com.semicolon.backend.domain.admin.dto.DashboardCountDTO;
import com.semicolon.backend.domain.admin.dto.DashboardDTO;
import com.semicolon.backend.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping("/approvals")
    public ResponseEntity<List<DashboardDTO>> getList(){
        return ResponseEntity.ok(service.getList());
    }

    @GetMapping("/count")
    public ResponseEntity<DashboardCountDTO> getCount(){
        return ResponseEntity.ok(service.getCount());
    }
}
