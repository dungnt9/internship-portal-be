package com.dungnguyen.user_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "user_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "auth_user_id", nullable = false, unique = true)
    private Integer authUserId;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    @OneToOne(mappedBy = "userRef", cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "userRef", cascade = CascadeType.ALL)
    private Company company;

    @OneToOne(mappedBy = "userRef", cascade = CascadeType.ALL)
    private Teacher teacher;

    @OneToOne(mappedBy = "userRef", cascade = CascadeType.ALL)
    private Admin admin;
}