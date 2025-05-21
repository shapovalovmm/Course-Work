package com.project.SH.model.impl;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;


// ВЛАСТИВОСТІ КАЗКИ
@Data
@Entity
public class Tale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String content;
    private int likes;

    @ManyToMany(mappedBy = "likedTales")
    private Set<User> likedBy = new HashSet<>();

}
