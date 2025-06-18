package com.demo.book.springtdd.domain;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Shopper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopKey;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String hashedPassword;

}
