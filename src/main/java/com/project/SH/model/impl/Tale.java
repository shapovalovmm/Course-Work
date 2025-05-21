package com.project.SH.model.impl;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;


// ВЛАСТИВОСТІ КАЗКИ
@Getter
@Setter
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tale)) return false;
        Tale tale = (Tale) o;
        return id != null && id.equals(tale.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

