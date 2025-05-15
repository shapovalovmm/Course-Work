package com.project.SH.model.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.project.SH.model.IConsumer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@DiscriminatorValue("CONSUMER")
@JsonTypeName("CONSUMER")
// ОБЛІКОВИЙ ЗАПИС ЗАРЕЄСТРОВАНОГО КОРИСТУВАЧА
public class Consumer  extends User implements IConsumer{
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
}
