package com.project.SH.service.impl;

import com.project.SH.model.impl.Tale;
import com.project.SH.model.impl.User;
import com.project.SH.repository.TaleRepository;
import com.project.SH.service.ITaleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaleService implements ITaleService {

    private final TaleRepository repository;

    public TaleService(TaleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tale> findAllTales() {
        return List.of();
    }

    @Override
    public Tale saveTale(Tale tale) {
        return null;
    }

    @Override
    public Tale findByID(int TaleID) {
        return null;
    }

    @Override
    public Tale updateTale(Tale tale) {
        return null;
    }

    @Override
    @Transactional
    public void deleteTale(int TaleID) {

    }
}

