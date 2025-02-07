package com.example.server.entity.user;

import com.example.server.factory.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password, UserType.ADMIN);
    }
}