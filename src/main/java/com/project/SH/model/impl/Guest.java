package com.project.SH.model.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GUEST")
@JsonTypeName("GUEST")
public class Guest extends User{
}
