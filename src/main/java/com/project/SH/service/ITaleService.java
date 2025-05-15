package com.project.SH.service;

import com.project.SH.model.impl.Tale;


import java.util.List;

public interface ITaleService {
    List<Tale> findAllTales();
    Tale saveTale(Tale tale);
    Tale findByID(int taleID);
    Tale updateTale(Tale tale);
    void deleteTale(int taleID);
}

