package com.project.SH.service;

import com.project.SH.model.impl.Admin;
import com.project.SH.model.impl.User;

import java.util.List;

public interface IAdminService {
    List<Admin> findAllAdmins();
    Admin saveAdmin(Admin admin);
    Admin findByID(int userID);
    Admin updateAdmin(Admin admin);
    void deleteAdmin(int userID);
}
