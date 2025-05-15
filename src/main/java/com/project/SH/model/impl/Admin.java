package com.project.SH.model.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.project.SH.model.IAdmin;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@DiscriminatorValue("ADMIN")
@JsonTypeName("ADMIN")

// ОБЛІКОВИЙ ЗАПИС АДМІНІСТРАТОРА
public class Admin extends User implements IAdmin {
    @OneToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tale_id")
    )
    List<Tale> favorites;
    @OneToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tale_id")
    )
    List<Tale> likes;

    @Override
    public void deleteTale(int taleID) {

    }

    @Override
    public Tale editTale(Tale tale) {
        return null;
    }
}
