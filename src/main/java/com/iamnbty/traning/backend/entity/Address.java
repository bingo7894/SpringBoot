package com.iamnbty.traning.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "m_address")
public class Address extends BaseEntity {

    @Column(length = 120)
    private String line1;

    @Column(length = 120)
    private  String line2;

    @Column(length = 120)
    private String zipcode;

    @ManyToOne
    @JoinColumn(name="m_user_id",nullable = false)
    private User user;
}


