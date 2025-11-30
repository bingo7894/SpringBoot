package com.iamnbty.traning.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "m_user")
public class User extends BaseEntity {

    @Column(nullable = false,unique = true,length = 60)
    private String email;
    
    @Column(nullable = false,length = 120)
    private  String password;

    @Column(nullable = false,length = 120)
    private String name;

}


