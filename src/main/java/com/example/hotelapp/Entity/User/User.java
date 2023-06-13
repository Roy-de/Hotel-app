package com.example.hotelapp.Entity.User;

import com.example.hotelapp.Entity.Roles.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user_details")
@Getter
@Setter
public class User {
    public User() {
    }

    public User(String username, String name, String password, String email) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username",nullable = false)
    @Size(max = 20)
    private String username;
    @Column(name = "name",nullable = false)
    @Size(max = 20)
    private String name;
    @Size(max = 20)
    @Column(name = "password",nullable = false)
    @Size(max = 20)
    private String password;
    @Size(max = 20)
    @Column(name = "email",nullable = false)
    private String email;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name ="user_roles" ,joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>();
}
