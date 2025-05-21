package com.project.SH.model.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("ADMIN")
@JsonTypeName("ADMIN")
// ОБЛІКОВИЙ ЗАПИС АДМІНІСТРАТОРА
public class Admin extends User {
    public void deleteTale(int taleID) {
    }
    public Tale editTale(Tale tale) {
        return null;
    }
}
