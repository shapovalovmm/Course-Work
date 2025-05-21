package com.project.SH.repository;

import com.project.SH.model.impl.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserUsername(String username);
    boolean existsByUserUsernameAndTaleId(String username, Long taleId);
}