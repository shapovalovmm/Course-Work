package com.project.SH.model;

import com.project.SH.model.impl.Tale;

public interface IAdmin {
    void deleteTale(int taleID);
    Tale editTale(Tale tale);
}
