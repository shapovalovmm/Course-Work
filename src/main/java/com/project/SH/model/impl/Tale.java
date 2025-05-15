package com.project.SH.model.impl;

import com.project.SH.model.ITale;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Tales")
// ВЛАСТИВОСТІ КАЗКИ
public class Tale implements ITale {
    @Id
    @GeneratedValue
    long taleID;
    String text;
    String description;
    int likes;
    String name;
    String author;
}
