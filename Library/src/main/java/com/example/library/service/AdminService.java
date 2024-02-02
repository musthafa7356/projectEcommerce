package com.example.library.service;

import com.example.library.dto.AdminDto;
import com.example.library.model.Admin;

public interface AdminService {

    Admin save(AdminDto adminDto);
    Admin findByUsername(String username);
}