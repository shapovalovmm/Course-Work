package com.project.SH.service.impl;

import com.project.SH.model.impl.Admin;
import com.project.SH.model.impl.User;
import com.project.SH.repository.AdminRepository;
import com.project.SH.repository.UserRepository;
import com.project.SH.service.IAdminService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin findByID(int userID) {
        return adminRepository.findById((long) userID).orElse(null);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public void deleteAdmin(int adminID) {
        adminRepository.deleteById((long) adminID);
    }
}
