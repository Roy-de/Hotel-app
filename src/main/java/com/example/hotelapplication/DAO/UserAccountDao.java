package com.example.hotelapplication.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDao {
    private String name;
    private String password;
    private String username;
    private String email;
}
