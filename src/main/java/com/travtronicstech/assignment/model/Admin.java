package com.travtronicstech.assignment.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
public class Admin {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    // UUID for the admin
    private UUID id;

    private String name;
    private String email;
    private String password;

    private String role = "ADMIN";  // Admin role is predefined
}
