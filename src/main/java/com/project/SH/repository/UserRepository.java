package com.project.SH.repository;

import com.project.SH.model.impl.Admin;
import com.project.SH.model.impl.Consumer;
import com.project.SH.model.impl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // Найти всех пользователей по имени (пример)
    List<User> findByUsername(String username);

    // Явно получить всех Consumer'ов
    @Query("SELECT u FROM User u WHERE TYPE(u) = Consumer")
    List<Consumer> findAllConsumers();

    // Явно получить всех Admin'ов
    @Query("SELECT u FROM User u WHERE TYPE(u) = Admin")
    List<Admin> findAllAdmins();
}
