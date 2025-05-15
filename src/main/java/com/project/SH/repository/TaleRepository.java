package com.project.SH.repository;

import com.project.SH.model.impl.Tale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaleRepository extends JpaRepository<Tale, Long> {
}
