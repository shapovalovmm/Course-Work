package com.project.SH.model.impl;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.project.SH.model.IUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "user_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = Consumer.class, name = "CONSUMER"),
        @JsonSubTypes.Type(value = Guest.class, name = "GUEST")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Table(name = "Users")
@Data
// ОБЛІКОВИЙ ЗАПИС ГОСТЯ
public abstract class User implements IUser {
    @Id
    @GeneratedValue
    long userID;
    private String username;
    private String password;

}
